/**
 * 共通
 */
const Url = Object.freeze({
    search: 'http://localhost:18090/holiday/search/{year}',
    update: 'http://localhost:18090/holiday/update',
});
function callApi(method, url, callbackHandler) {
    const httpRequest = new XMLHttpRequest();
    httpRequest.responseType = 'json'
    httpRequest.open(method, url, true);
    httpRequest.setRequestHeader("Content-Type", "application/json");
    httpRequest.onload  = (data) => {
        try {
            // const response = JSON.parse(data.target.response);
            callbackHandler(data.target.response);
        }
        catch (e) {
            alert(`Error: ` + e.message);
        }
    };
    httpRequest.send();
}
/**
 * Update
 */
function funcUpdate() {
    function handler(response) {
        const data = response.data;
        alert('更新しました。');
    }
    callApi("POST", Url.update, handler);
}
/**
 * Search
 */
function funcSearch() {
    const val = document.getElementById("year").value;
    document.getElementById("restList").innerHTML = '';
    document.getElementById("holiday").value = '';
    getSearch(val);
}
function getSearch(value) {
    function handler(response) {
        const list = response.data;
        for(var i = 0; i < list.length; i++){
            let op = document.createElement("option");
            op.value = list[i].date + ":" + list[i].dayOfWeek;
            op.text = list[i].note;
            document.getElementById("restList").appendChild(op);
        }
        document.getElementById("holiday").value = list[0].date + ":" + list[0].dayOfWeek;
    }
    callApi("GET", Url.search.replace('{year}', value), handler);
}

function onChnageHandler(selectItem) {
    var idx = selectItem.selectedIndex;
    document.getElementById("holiday").value = selectItem.options[idx].value;
}






