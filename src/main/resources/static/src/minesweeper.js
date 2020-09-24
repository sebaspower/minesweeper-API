const userUrl = 'http://localhost:5000/users';
const gameUrl = 'http://localhost:5000/games';


function initMinesweeper(){
    window.userId = null;
    window.gameIdToResume = null;
    window.gameId = null;
    hide('resumeParameters');
    hide('playParameters');
    hide('restart');
    hide('result');
}

function restart(){
    let userId = window.userId;
    initMinesweeper();
    initUserState(userId);
    getPreviousGames();
}

function init(){
    let xhr = new XMLHttpRequest();
    function userCbk(){
        if (xhr.readyState === 4 && xhr.status === 200) {
            initUserState(JSON.parse(this.responseText).id);
            getPreviousGames();
        }
    };
    let data = JSON.stringify({ "name": name.value});
    send(xhr,"PUT", userUrl, data, userCbk);
}

function resume(){
    if(window.gameIdToResume){
        let xhr = new XMLHttpRequest();
        function setGame(){
            if (xhr.readyState === 4 && xhr.status === 200)
                playingState(JSON.parse(this.responseText));
        }
        send(xhr,"GET", gameUrl+'/'+ window.gameIdToResume, null, setGame);
    }
}

function play(){
    let totalRow = document.querySelector('#rows');
    let totalCol = document.querySelector('#cols');
    let totalMines = document.querySelector('#mines');
    // input validation.
    if(totalRow.value === '' || totalCol.value === '' || totalMines.value === ''){
        console.log("Rows, Cols or Mines are empty");
        return;
    }
    if (window.userId) {
        let xhr = new XMLHttpRequest();
        function userCbk(){
            if (xhr.readyState === 4 && xhr.status === 200)
                playingState(JSON.parse(this.responseText));
        };
        let data = JSON.stringify({
            "totalRow": parseInt(document.querySelector('#rows').value),
            "totalCol": parseInt(document.querySelector('#cols').value),
            "totalMines": parseInt(document.querySelector('#mines').value),
            "userId": window.userId
        });
        send(xhr,"POST", gameUrl, data, userCbk);
    }
    else console.error("User Not defined");
}

function getPreviousGames(){
    let xhr = new XMLHttpRequest();
    function setResumeGame(){
        if (xhr.readyState === 4 && xhr.status === 200) {
            window.gameIdToResume = JSON.parse(this.responseText).id;
            show('resumeParameters');
            document.getElementById("resumeButton").disabled = false;
        }
    }
    send(xhr,"GET", gameUrl+'/resume/'+ window.userId, null, setResumeGame);
}

function showCell(row, col){
    let url = gameUrl+'/'+ window.gameId +'/show/'+row+','+col;
    sendClick(url, row, col);
}

function flagMine(row, col){
    let url = gameUrl+'/'+ window.gameId +'/mine/'+row+','+col;
    sendClick(url, row, col);
}

function sendClick(url, row, col){
    let xhr = new XMLHttpRequest();
    function refreshBoard(){
        if (xhr.readyState === 4 && xhr.status === 200) {
            var data = JSON.parse(this.responseText);
            updateBoard(data);
        }
    }
    send(xhr,"PUT", url, null, refreshBoard);
}


function send(xhr, method, url, data, callback){
    xhr.open(method, url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = callback;
    xhr.send(data);
}

function hide(idName){
    document.getElementById(idName).style.display = 'none';
    document.getElementById(idName).style.hidden = true;
}

function show(idName){
    document.getElementById(idName).style.display = 'block';
    document.getElementById(idName).style.hidden = false;
}

function gameOver(){
    clearInterval(window.t);
    document.getElementById("timeElapsed").innerHTML = "GAME OVER";
    document.getElementById("timeElapsed").style.color= "red";
}
function playingState(data){
    window.gameId = data.id;
    window.timeStarted = data.started;
    document.getElementById('cols').value = data.totalCol;
    document.getElementById('rows').value = data.totalRow;
    document.getElementById('mines').value = data.totalMines;
    document.getElementById('rows').disabled = true;
    document.getElementById('cols').disabled = true;
    document.getElementById('mines').disabled = true;
    document.getElementById('resumeButton').disabled = true;
    document.getElementById('playButton').disabled = true;
    show('restart');
    show('result');
    updateBoard(data);
    window.t = setInterval(function() {
        if(window.timeStarted) {
            var now = new Date().getTime();
            var distance = now - window.timeStarted;
            var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
            var seconds = Math.floor((distance % (1000 * 60)) / 1000);
            document.getElementById("timeElapsed").innerHTML = "TIME ELAPSED:"+ hours + "h "
                + minutes + "m " + seconds + "s ";
            document.getElementById("timeElapsed").style.color = 'blue';
        }
    }, 1000);
}

function initUserState(userId){
    window.userId  = userId;
    document.getElementById('start').disabled = true;
    document.getElementById('username').disabled = true;
    document.getElementById('cols').value = '';
    document.getElementById('rows').value = '';
    document.getElementById('mines').value = '';
    document.getElementById('rows').disabled = false;
    document.getElementById('cols').disabled = false;
    document.getElementById('mines').disabled = false;
    show('playParameters');
    document.getElementById('playButton').disabled = false;
    clearInterval(window.t);
}

function updateBoard(data) {
    if(data.gameOver) {
        gameOver();
    } else{
        if (data) {
            document.querySelector('.flaggedMines').innerHTML = "Flagged Mines:"+ data.totalPossibleMines;
            var table = document.getElementById('myTable')
            table.innerHTML = "";
            for (var i = 0; i < data.board.length; i++) {
                var tr = table.insertRow(-1);
                for (var j = 0; j < data.board[i].length; j++) {
                    var tabCell = tr.insertCell(-1);
                    if ((data.board[i][j].possibleMine)) {
                        tabCell.innerHTML = '@';
                    } else {
                        if (data.board[i][j].show) {
                            if (data.board[i][j].nearMines == '0') tabCell.innerHTML = '.';
                            else tabCell.innerHTML = data.board[i][j].nearMines;
                        } else tabCell.innerHTML = '#';
                    }
                }
            }
            $("#myTable td").click(function (event) {
                var col = parseInt($(this).index());
                var row = parseInt($(this).parent().index());
                var clicked = document.getElementsByName('clickCell')[0];
                if (clicked.checked){
                    showCell(row, col);
                } else {
                    flagMine(row, col);
                }
            });
         }
    }
}




