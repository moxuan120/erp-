var oper = Request['oper'];
var type = Request['type'];

$(function () {
    var url = 'orders_list?t1.type=' + type;

    //订单申请，只有我的列表
    if (oper == 'myOrders') {
        url = 'orders_myList?t1.type=' + type;
    }
    //审核
    if (oper == 'doCheck') {
        url += "&t1.state=0";
    }
    //确认
    if (oper == 'doStart') {
        url += "&t1.state=1";
    }
    //入库
    if (oper == 'doInStore') {
        url += "&t1.state=2";
    }
    //出库
    if (oper == 'doOutStore') {
        url += "&t1.state=0";
    }
    //完成
    if (oper == 'isFinish') {
        if (type == 1) {
            url += "&t1.state=3";
        }
        if (type == 2) {
            url += "&t1.state=1";
        }
    }


    //订单表
    $("#grid").datagrid({
        url: url,
        singleSelect: true,
        pagination: true,
        pageSize:20,
        toolbar: '#tb',
        fitColumns: true,
        columns: getColumns(),
        onDblClickRow: function (rowIndex, rowData) {
            //给详情赋值
            $('#uuid').html(rowData.uuid);
            $('#suppliername').html(rowData.supplierName);
            $('#state').html(formatState(rowData.state));
            $('#sn').html(rowData.waybillsn);

            $('#createrName').html(rowData.createrName);
            $('#checkerName').html(rowData.checkerName);
            $('#starterName').html(rowData.starterName);
            $('#enderName').html(rowData.enderName);

            $('#createtime').html(rowData.createtime);
            $('#checktime').html(rowData.checktime);
            $('#starttime').html(rowData.starttime);
            $('#endtime').html(rowData.endtime);

            $('#itemgrid').datagrid('loadData', rowData.orderdetails);

            $('#ordersDlg').dialog('open');
        }
    });
    if (oper == 'myOrders') {
        //pagination自定义工具栏按钮
        var text = function () {
            if (type == 1) return '采购申请';
            else return '销售申请';
        };
        var pager = $('#grid').datagrid('getPager');	// get the pager of datagrid
        pager.pagination({
            buttons: [{
                iconCls: 'icon-add',
                text: text,
                handler: function () {
                    openAdd()
                }
            }]
        });
    }

    //订单详情列表
    $('#itemgrid').datagrid({
        columns: [[
            {field: 'goodsuuid', title: '商品编号', width: 100},
            {field: 'goodsname', title: '商品名称', width: 100},
            {field: 'price', title: '价格', width: 100},
            {field: 'num', title: '数量', width: 100},
            {field: 'money', title: '金额', width: 100},
            {field: 'state', title: '状态', width: 100, formatter: formatDetailState}
        ]],
        singleSelect: true,
        fitColumns: true
    });

    //绑定出入库事件
    if (oper == 'doInStore' || oper == 'doOutStore') {
        $('#itemgrid').datagrid({
            onDblClickRow: function (rowIndex, rowData) {
                $('#itemid').val(rowData.uuid);
                $('#goodsuuid').html(rowData.goodsuuid);
                $('#goodsname').html(rowData.goodsname);
                $('#num').html(rowData.num);
                $('#itemDlg').dialog('open');
            }
        });
    }

    //出入库窗口
    if(type == 1){
        var itemDlgTitle = '入库';
    }else {
        var itemDlgTitle = '出库';
    }
    $('#itemDlg').dialog({
        title: itemDlgTitle + '明细',
        height: 200,
        width: 300,
        modal: true,
        closed: true,
        buttons: [
            {
                text: itemDlgTitle,
                iconCls: 'icon-save',
                handler: doInOutStore
            }
        ]
    });


    var toolbar = new Array();
    //添加审核的按钮
    if (oper == 'doCheck') toolbar.push({text: '审核', iconCls: 'icon-search', handler: doCheck});

    //添加确认的按钮
    if (oper == 'doStart') toolbar.push({text: '确认', iconCls: 'icon-search', handler: doStart});

    //添加导出按钮
    toolbar.push({text: '导出', iconCls: 'icon-excel', handler: function () {
        $.download("orders_exportExcel", {id:$('#uuid').html()});
    }});

    if(type == 2){
        //物流详情
        toolbar.push({text:'物流详情',iconCls:'icon-search',handler:function(){
            if($('#sn').html() != ''){
                $('#waybillDlg').dialog('open');
                $('#waybillGrid').datagrid({
                    url: 'orders_waybillDetailList?waybillSn=' + $('#sn').html()
                });
            }else{
                $.messager.alert("提示","当前还没有物流信息!",'info');
            }
        }});
    }

    //订单详情
    $('#ordersDlg').dialog({
        toolbar: toolbar,
        buttons: [{
            iconCls: 'icon-cancel',
            text: '关闭',
            handler: function () {
                //清空数据
                $('#uuid').html(null);
                $('#suppliername').html(null);
                $('#state').html(null);
                $('#sn').html(null);

                $('#createrName').html(null);
                $('#checkerName').html(null);
                $('#starterName').html(null);
                $('#enderName').html(null);

                $('#createtime').html(null);
                $('#checktime').html(null);
                $('#starttime').html(null);
                $('#endtime').html(null);
                $('#ordersDlg').dialog('close')
            }
        }]
    });

    //详情表格头信息
    if(type ==1) {
        $("#orderdetailTable").append("<tr>" +
            "<td class='bg' >下单人</td>" +
            "<td id='createrName'></td>" +
            "<td class='bg' >审核人</td>" +
            "<td id='checkerName'></td>" +
            "<td class='bg' >确认人</td>" +
            "<td id='starterName'></td>" +
            "<td class='bg' >库管员</td>" +
            "<td id='enderName'></td>" +
            "</tr><tr>" +
            "<td class='bg noBorderBottom'>下单时间</td>" +
            "<td class='noBorderBottom' id='createtime'></td>" +
            "<td class='bg noBorderBottom'>审核时间</td>" +
            "<td class='noBorderBottom' id='checktime'></td>" +
            "<td class='bg noBorderBottom'>确认时间</td>" +
            "<td class='noBorderBottom' id='starttime'></td>" +
            "<td class='bg noBorderBottom'>入库时间</td>" +
            "<td class='noBorderBottom' id='endtime'></td>" +
            "</tr>")
    }
    if(type ==2) {
        $("#orderdetailTable").append("<tr>" +
            "<td class='bg noBorderBottom'>下单人</td>" +
            "<td class='noBorderBottom' id='createrName'></td>" +
            "<td class='bg noBorderBottom'>下单时间</td>" +
            "<td class='noBorderBottom' id='createtime'></td>" +
            "<td class='noBorderBottom' >库管员</td>" +
            "<td class='noBorderBottom' id='enderName'></td>" +
            "<td class='bg noBorderBottom'>出库时间</td>" +
            "<td class='noBorderBottom' id='endtime'></td>" +
            "</tr>")
    }

    //物流详情
    $("#waybillGrid").datagrid({
        columns:[[
            {field:'exedate',title:'执行日期',width:100},
            {field:'exetime',title:'执行时间',width:100},
            {field:'info',title:'信息',width:100}
        ]],
        fitColumns:true,
        rownumbers:true
    })
});

function getColumns() {
    //采购
    if (type == 1) {
        return [[
            {field: 'uuid', title: '编号', width: 100},
            {field: 'createtime', title: '生成时间', width: 120},
            {field: 'checktime', title: '检查时间', width: 120},
            {field: 'starttime', title: '开始时间', width: 120},
            {field: 'endtime', title: '结束时间', width: 120},
            {field: 'createrName', title: '下单员', width: 100},
            {field: 'checkerName', title: '审查员', width: 100},
            {field: 'starterName', title: '采购员', width: 100},
            {field: 'enderName', title: '库管员', width: 100},
            {field: 'supplierName', title: '供应商', width: 100},
            {field: 'totalmoney', title: '总金额', width: 100},
            {field: 'state', title: '状态', width: 100, formatter: formatState},
            {field: 'waybillsn', title: '运单号', width: 100}
        ]];
    } //采购
    if (type == 2) {
        return [[
            {field: 'uuid', title: '编号', width: 100},
            {field: 'createtime', title: '生成时间', width: 120},
            {field: 'endtime', title: '入库时间', width: 120},
            {field: 'createrName', title: '下单员', width: 100},
            {field: 'enderName', title: '库管员', width: 100},
            {field: 'supplierName', title: '客户', width: 100},
            {field: 'totalmoney', title: '合计金额', width: 100},
            {field: 'state', title: '状态', width: 100, formatter: formatState},
            {field: 'waybillsn', title: '运单号', width: 100}
        ]];
    }
}

/**
 * 审核
 */
function doCheck() {
    $.messager.confirm('确认', '确认要审核吗？', function (yes) {
        if (yes) {
            $.post('orders_doCheck', {id: $('#uuid').html()}, function (data) {
                $.messager.alert('提示', data.message, 'info', function () {
                    if (data.success) {
                        //关闭审核窗口
                        $('#ordersDlg').dialog('close');
                        //刷新表格
                        $('#grid').datagrid('reload');
                    }
                });
            }, 'json');
        }
    });
}

/**
 * 确认
 */
function doStart() {
    $.messager.confirm('确认', '确定要确认吗？', function (yes) {
        if (yes) {
            $.post('orders_doStart', {id: $('#uuid').html()}, function (data) {
                $.messager.alert('提示', data.message, 'info', function () {
                    if (data.success) {
                        //关闭审核窗口
                        $('#ordersDlg').dialog('close');
                        //刷新表格
                        $('#grid').datagrid('reload');
                    }
                });
            }, 'json');
        }
    });
}

/**
 * 入库
 */
function doInOutStore() {
    if(type == 1){
        var message = "确定要入库吗？";
        var url = 'orderDetail_doInStore';
    }else {
        var message = "确定要出库吗？";
        var url = 'orderDetail_doOutStore';
    }
    $.messager.confirm('确认', message, function (yes) {
        if (yes) {
            var submitData = $('#itemForm').serializeJSON();
            if (submitData.storeuuid == '') {
                $.messager.alert('提示', '请选择仓库', 'info');
                return;
            }
            $.post(url,submitData,function (data) {
                $.messager.alert('提示', data.message, 'info', function () {
                    if (data.success) {
                        //关掉入库窗口
                        $('#itemDlg').dialog('close');
                        //更新明细里的状态为已入库
                        var row = $('#itemgrid').datagrid('getSelected');
                        row.state = '1';
                        //刷新明细表格
                        var gridData = $('#itemgrid').datagrid('getData');
                        $('#itemgrid').datagrid('loadData', gridData);
                        //关闭详情窗口
                        var flag = true;
                        $.each(gridData.rows, function (i, r) {
                            if (r.state == 0) {
                                flag = false;
                                return false;
                            }
                        });
                        if (flag == true) {
                            //关闭窗口
                            $('#ordersDlg').dialog('close');
                            //刷新表格
                            $('#grid').datagrid('reload');
                        }
                    }
                });
            },'json');
        }
    });

}


/**
 * 订单状态的格式化器
 * @param value
 * @returns {String}
 */
function formatState(value) {
    //采购: 0:未审核 1:已审核, 2:已确认, 3:已入库；销售：0:未出库 1:已出库
    if (type == 1) {
        switch (value * 1) {
            case 0:
                return '未审核';
            case 1:
                return '已审核';
            case 2:
                return '已确认';
            case 3:
                return '已入库';
            case 4:
            	return '申请退货';
            case 5:
            	return '确认退货';
            case 6:
            	return '退货完成';
            case 7:
            	return '部分申请退货';
            case 8:
            	return '部分确认退货';
            case 9:
            	return '部分退货完成';
            default:
                return '';
        }
    }
    if (type == 2) {
        switch (value * 1) {
            case 0:
                return '未出库';
            case 1:
                return '已出库';
            case 4:
            	return '申请退货';
            case 5:
            	return '确认退货';
            case 6:
            	return '退货完成';
            case 7:
            	return '部分申请退货';
            case 8:
            	return '部分确认退货';
            case 9:
            	return '部分退货完成';
            default:
                return '';
        }
    }
}

function formatDetailState(value) {
    //采购：0=未入库，1=已入库  销售：0=未出库，1=已出库
    if (type == 1) {
        switch (value * 1) {
            case 0:
                return '未入库';
            case 1:
                return '已入库';
            case 2:
            	return '申请退货';
            case 3:
            	return '确认退货';
            case 4:
            	return '退货完成';
            default:
                return '';
        }
    }
    if (type == 2) {
        switch (value * 1) {
            case 0:
                return '未出库';
            case 1:
                return '已出库';
            case 2:
            	return '申请退货';
            case 3:
            	return '确认退货';
            case 4:
            	return '退货完成';
            default:
                return '';
        }
    }
}