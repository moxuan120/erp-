$(function () {
    //列表
    $("#grid").datagrid({
        url: 'storedetail_list',
        singleSelect: true,
        pagination: true,
        pageSize:20,
        toolbar: '#tb',
        fitColumns: true,
        columns: [[
            {field: 'uuid', title: '编号', width: 100},
            {field: 'storeName', title: '仓库编号', width: 100},
            {field: 'goodsName', title: '商品编号', width: 100},
            {field: 'num', title: '数量', width: 100},
        ]]
    });
    //库存操作记录
    $('#operGrid').datagrid({
        singleSelect: true,
        fitColumns: true,
        pagination: true,
        columns: [[
            {field: 'uuid', title: '编号', width: 100},
            {field: 'empName', title: '操作员工', width: 100},
            {field: 'opertime', title: '操作时间', width: 120},
            {field: 'storeName', title: '仓库', width: 100},
            {field: 'goodsName', title: '商品', width: 100},
            {field: 'num', title: '数量', width: 100},
            {
                field: 'type', title: '操作类型', width: 100, formatter: function (value) {
                	if (value == 1) return '采购入库';
                    if (value == 2) return '销售出库';
                    if (value == 3) return '退货入库';
                    if (value == 4) return '退货出库';
                    if (value == 5) return '盘盈入库';
                    if (value == 6) return '盘亏出库';
            }
            }
        ]]
    });
});


function onDblClickRow(rowIndex, rowData) {
	var url = 'storeoper_list?t1.goodsuuid='+rowData.goodsuuid+'&t1.storeuuid='+rowData.storeuuid;
    $('#operGrid').datagrid({
    	url:url
    });

    $('#operDlg').dialog('open');
}

function refresh() {
    var data = $("#searchForm").serializeJSON();
    $("#grid").datagrid('load', data)
}

function clearSearchForm() {
    $("#searchForm").form('clear');
}