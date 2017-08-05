var oper = Request['oper'];
var olduuid = "";

$(function () {
    var url = 'returnorders_list?t1.type=1';

    //订单申请，只有我的列表
    if (oper == 'myOrders') {
        url = 'returnorders_myList?t1.type=1';
    }
    //审核
    if (oper == 'doCheck') {
        url += "&t1.state=0";
    }
    //出库
    if (oper == 'doOutStore') {
        url += "&t1.state=1";
    }
    //完成
    if (oper == 'isFinish') {
        url += "&t1.state=2";
    }


    //订单表
    $("#grid").datagrid({
        url: url,
        singleSelect: true,
        pagination: true,
        pageSize:20,
        fitColumns: true,
        columns: [[
                   {field: 'uuid', title: '编号', width: 100},
                   {field: 'createtime', title: '录入时间', width: 120},
                   {field: 'checktime', title: '检查时间', width: 120},
                   {field: 'endtime', title: '结束时间', width: 120},
                   {field: 'createrName', title: '录入员', width: 100},
                   {field: 'checkerName', title: '审核员', width: 100},
                   {field: 'enderName', title: '库管员', width: 100},
                   {field: 'supplierName', title: '供应商', width: 100},
                   {field: 'totalmoney', title: '总金额', width: 100},
                   {field: 'state', title: '状态', width: 100, formatter: formatState},
                   {field: 'orders', title: '原订单号', width: 100,formatter:function(data){
                	   return data.uuid;
                   }},
                   {field: 'waybillsn', title: '运单号', width: 100}
               ]],
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

            $('#itemgrid').datagrid('loadData', rowData.returnorderdetails);

            $('#ordersDlg').dialog('open');
        }
    });
    if (oper == 'myOrders') {
        $('#grid').datagrid({
			toolbar:[{
				iconCls : 'icon-edit',
				text : '采购退货申请',
				handler : function() {
					$('#addOrdersDlg').dialog('open');
				}
			}],
		})
    }
    
    //4.开新退货订单窗口初始化
	$('#addOrdersDlg').dialog({
		title : '选择要退货的订单',
		width : 800,
		height : 400,
		modal : true,
		closed : true,
		buttons : [ {
			text : '关闭',
			iconCls : 'icon-save',
			handler : function() {
				$('#addOrdersDlg').dialog("close");
			},
		},{
			text : '退货',
			iconCls : 'icon-save',
			handler : function() {
				submitReturn();
				$('#addOrdersDlg').dialog("close");
			},
		}
		]
	})
	$('#oldOrdersCombogrid').combogrid({    
    panelWidth:450,    
    idField:'uuid',    
    textField:'uuid',    
    url:'orders_list?t1.type=1&t1.state=3',    
    columns:[[    
        {field:'uuid',title:'uuid',width:60},    
        {field:'createtime',title:'createtime',width:100},    
        {field:'endtime',title:'endtime',width:120},    
        {field:'orderdetails',title:'orderdetails',width:100}
    ]],
    onClickRow: function (rowIndex, rowData) {
       alert(rowData.uuid);
       olduuid = rowData.uuid;
    }
});  

	

    //2.订单详情列表
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
    if (oper == 'doOutStore') {
        $('#itemgrid').datagrid({
            onDblClickRow: function (rowIndex, rowData) {
                $('#id').val(rowData.uuid);
                $('#goodsuuid').html(rowData.goodsuuid);
                $('#goodsname').html(rowData.goodsname);
                $('#num').html(rowData.num);
                $('#itemDlg').dialog('open');
            }
        });
    }

    //出入库窗口
    $('#itemDlg').dialog({
        title: '退货出库明细',
        height: 200,
        width: 300,
        modal: true,
        closed: true,
        buttons: [
            {
                text: '退货',
                iconCls: 'icon-save',
                handler: doOutStore
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


/**
 * 审核
 */
function doCheck() {
    $.messager.confirm('确认', '确认要审核吗？', function (yes) {
        if (yes) {
            $.post('returnorders_doCheck', {id: $('#uuid').html()}, function (data) {
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
 * 退货出库
 */
function doOutStore() {
    var message = "确定要同意退货吗？";
    var url = 'returnorderdetail_doOutStore';
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
    //采购: 0:未审核 1:已审核, 2:已退货出库,
    switch (value * 1) {
        case 0:
            return '未审核';
        case 1:
            return '已审核';
        case 2:
            return '已退货';
        default:
            return '';
    }
}

function formatDetailState(value) {
    //采购：0=未入库，1=已入库  
    switch (value * 1) {
        case 0:
            return '未出库';
        case 1:
            return '已出库';
        default:
            return '';
    }
}

function submitReturn(){
	alert(olduuid);
	 $.post(
			 'returnorders_add?t1.type=1',
			 {"olduuid":olduuid},
			 function (data) {
		         $.messager.alert('提示', data.message, 'info', function () {
		             if (data.success) {
		                 //清空供应商
		                 $('#orderForm').form('clear');
		                 //清空表格
		                 $('#ordersGrid').datagrid('loadData',{total:0,rows:[],footer:[{num: '合计', money:'0'}]});
		                 //关闭窗口
		                 $("#editOrders").window("close");
		                 //刷新列表
		                 $("#grid").datagrid("reload");
		             }
		         });
			 },
	 		'json'
	 )
}