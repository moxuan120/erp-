var url = location.search;
var Request = new Object();
if (url.indexOf("?") != -1) {
    var str = url.substr(1);
    //str = type=1&oper=add
    strs = str.split("&");
    //["type=1", "oper=add"]
    for (var i = 0; i < strs.length; i++) {
        Request[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
    }
}