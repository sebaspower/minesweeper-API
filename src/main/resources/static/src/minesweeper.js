const userUrl = 'http://localhost:5000/users';
const gameUrl = 'http://localhost:5000/games';


function initMinesweeper(){
    window.userId = null;
    window.gameIdToResume = null;
    window.gameId = null;
    window.flaggedMines = 0;
    hide('resumeParameters');
    hide('playParameters');
    hide('restart');
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
            result.innerHTML = "GameToResume:"+ window.gameIdToResume;
            show('resumeParameters');
            document.getElementById("resumeButton").disabled = false;
        }
    }
    let result = document.querySelector('.resumeGame');
    send(xhr,"GET", gameUrl+'/resume/'+ window.userId, null, setResumeGame);
}

function showCell(row, col){
    let xhr = new XMLHttpRequest();
    function refreshBoard(){
        if (xhr.readyState === 4 && xhr.status === 200) {
            var data = JSON.parse(this.responseText);
            if(data.gameOver) gameOver();
            updateBoard(data.board);
        }
    }
    let result = document.querySelector('.resumeGame');
    send(xhr,"PUT", gameUrl+'/'+ window.gameId +'/show/'+row+','+col , null, refreshBoard);
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
    window.flaggedMines = data.totalPossibleMines;
    window.timeStarted = data.started;
    document.querySelector('.resultGame').innerHTML = "gameId:"+ window.gameId;
    document.querySelector('.flaggedMines').innerHTML = "Flagged:"+ window.flaggedMines;
    document.getElementById('cols').value = data.totalCol;
    document.getElementById('rows').value = data.totalRow;
    document.getElementById('mines').value = data.totalMines;
    document.getElementById('rows').disabled = true;
    document.getElementById('cols').disabled = true;
    document.getElementById('mines').disabled = true;
    document.getElementById('resumeButton').disabled = true;
    document.getElementById('playButton').disabled = true;
    show('restart');
    updateBoard(data.board);
    window.t = setInterval(function() {
        if(window.timeStarted) {
            var now = new Date().getTime();
            var distance = now - window.timeStarted;
            var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
            var seconds = Math.floor((distance % (1000 * 60)) / 1000);
            document.getElementById("timeElapsed").innerHTML = "Time Elapsed:"+ hours + "h "
                + minutes + "m " + seconds + "s ";
            document.getElementById("timeElapsed").style.color = 'blue';
        }
    }, 1000);
}

function initUserState(userId){
    window.userId  = userId;
    document.querySelector('.resultUser').innerHTML = "UserId:"+ window.userId;
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
}


function updateBoard(board) {
    if(board) {
        var table = document.getElementById('myTable')
        table.innerHTML = "";
        for (var i = 0; i < board.length; i++) {
            var tr = table.insertRow(-1);
            for (var j = 0; j < board[i].length; j++) {
                var tabCell = tr.insertCell(-1);
                if(board[i][j].show) {
                    if ((board[i][j].possibleMine)) tabCell.innerHTML = 'X';
                    else{
                        if (board[i][j].nearMines == '0') tabCell.innerHTML = '.';
                        else tabCell.innerHTML = board[i][j].nearMines;
                    }
                }
                else tabCell.innerHTML = '#';
            }
        }
        $("#myTable td").click(function() {
            var col = parseInt( $(this).index());
            var row = parseInt( $(this).parent().index() );
            $("#result").html( "Row=" + row + "  ,  Col="+ col );
            showCell(row,col);
        });
    }
}



