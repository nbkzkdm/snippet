/**
 * 共通
 */
const Url = Object.freeze({
    search: 'http://localhost:18080/postalcode/search/{postalCode}',
    prefecture: 'http://localhost:18080/postalcode/prefecture/{prefecture}',
    cityTownVillage: 'http://localhost:18080/postalcode/cityTownVillage/{cityTownVillage}',
    update: 'http://localhost:18080/postalcode/update',
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
 * Search
 */
function funcSearch() {
    const val = document.getElementById("postalCode").value;
    getSearch(val);
}
function getSearch(value) {
    function handler(response) {
        const data = response.data[0];
        document.getElementById("address").value = data.prefectureName + data.cityTownVillage + data.streetName;
        document.getElementById("p_address").value = data.prefectureName;
        document.getElementById("c_address").value = data.cityTownVillage;
        document.getElementById("s_address").value = data.streetName;
    }
    callApi("GET", Url.search.replace('{postalCode}', value), handler);
}
/**
 * Prefecture
 */
function funcPrefecture() {
    const val = document.getElementById("prefectureId").value;
    document.getElementById("cityTownVillage").innerHTML = '';
    document.getElementById("streetName").innerHTML = '';
    document.getElementById("zipCode").value = '';
    getPrefecture(val);
}
function getPrefecture(value) {
    function handler(response) {
        const cityList = response.data.cityTownVillageList;
        for(var i = 0; i < cityList.length; i++){
            let op = document.createElement("option");
            op.value = cityList[i].cityTownVillage;
            op.text = cityList[i].cityTownVillage;
            document.getElementById("cityTownVillage").appendChild(op);
        }
    }
    callApi("GET", Url.prefecture.replace('{prefecture}', value), handler);
}
function funcCityTownVillage() {
    const val = document.getElementById("cityTownVillage").value;
    document.getElementById("streetName").innerHTML = '';
    document.getElementById("zipCode").value = '';
    getCityTownVillage(val);
}
function getCityTownVillage(value) {
    function handler(response) {
        const streetList = response.data.streetNameList;
        for(var i = 0; i < streetList.length; i++){
            let op = document.createElement("option");
            op.value = streetList[i].postalCode;
            op.text = streetList[i].streetName;
            document.getElementById("streetName").appendChild(op);
        }
    }
    callApi("GET", Url.cityTownVillage.replace('{cityTownVillage}', value), handler);
}
function onChnageHandler(selectItem) {
    var idx = selectItem.selectedIndex;
    document.getElementById("zipCode").value = selectItem.options[idx].value;
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







