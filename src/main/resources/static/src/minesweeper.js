const userUrl = 'http://localhost:5000/users';
const gameUrl = 'http://localhost:5000/games';


function initMinesweeper(){
    window.userId = null;
    window.gameIdToResume = null;
    window.gameId = null;
    window.board = null;
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
            if (xhr.readyState === 4 && xhr.status === 200) {
                playingState(JSON.parse(this.responseText));
            } else
            {
                console.log("Error resuming" + xhr.readyState);
            }
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
            if (xhr.readyState === 4 && xhr.status === 200) {
                playingState(JSON.parse(this.responseText));
            } else {
                console.log("Problem creating game");
            }

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
        } else {
            console.log("None game to resume");
        }
    }
    let result = document.querySelector('.resumeGame');
    send(xhr,"GET", gameUrl+'/resume/'+ window.userId, null, setResumeGame);
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

function playingState(data){
    window.gameId = data.id;
    window.board = data.board;
    window.flaggedMines = data.totalPossibleMines;
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
    updateBoard();
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


function updateBoard() {
    if(board) {
        var table = document.createElement("table");
        for (var i = 0; i < window.board.length; i++) {
            var tr = table.insertRow(-1);
            for (var j = 0; j < window.board[i].length; j++) {
                var tabCell = tr.insertCell(-1);
                if(window.board[i][j].show==false) {
                    if ((window.board[i][j].hasMine)) tabCell.innerHTML = 'X';
                    else{
                        tabCell.innerHTML = window.board[i][j].nearMines;
                        //if (window.board[i][j].nearMines == '0') tabCell.innerHTML = '';
                        //else tabCell.innerHTML = window.board[i][j].nearMines;
                    }
                }
                else tabCell.innerHTML = '#';

            }
        }.
        var divContainer = document.getElementById("showData");
        divContainer.innerHTML = "";
        divContainer.appendChild(table);
    }
}
