var oper=Request['oper'];
var type=Request['type'] * 1;

$(function(){

		var url = 'returnorders_list?t1.type='+type;
	
		//订单审核
		if(oper == 'doReturnCheck'){
			url +='&t1.state=0';
		}
		//销售退货订单入库
		if(oper=='doReturnInStore'){
			url+='&t1.state=1';
		}
		//完成
	    if (oper == 'isFinish') {
	    	url+='&t1.state=2';
	    }
	
	$('#grid').datagrid({
		url:url,
		columns:getColumns(),
		singleSelect: true,
		pagination: true,
		fitColumns: true,
		onDblClickRow:function(rowIndex,rowData){
			$('#returnordersDlg').dialog('open');
        	$('#uuid').html(rowData.uuid);
        	$('#supplierName').html(rowData.supplierName);
			$('#state').html(formatState(rowData.state));
			$('#createrName').html(rowData.createrName);
			$('#checkerName').html(rowData.checkerName);
			$('#starterName').html(rowData.starterName);
			$('#enderName').html(rowData.enderName);
			$('#createtime').html(rowData.createtime);
			$('#checktime').html(rowData.checktime);
			$('#starttime').html(rowData.starttime);
			$('#endtime').html(rowData.endtime);
			$('#itemgrid').datagrid('loadData',rowData.returnorderdetails);
		}
		
	});
	
	$('#addReturnOrdersDlg').dialog({
		title:'销售退货申请',
		width:710,
		height:400,
		modal:true,
		closed:true
	});
	
	//明细表格
	$('#itemgrid').datagrid({
		columns:[[
					{field:'uuid',title:'编号',width:100},
					{field:'goodsuuid',title:'商品编号',width:100},
					{field:'goodsname',title:'商品名称',width:100},
					{field:'price',title:'价格',width:100},
					{field:'num',title:'数量',width:100},
					{field:'money',title:'金额',width:100},
					{field:'state',title:'状态',width:100,formatter:formatDetailState}
				]],
				singleSelect:true,
				fitColumns:true
	})
	
	//订单详情窗口 的工具栏
	var orderDlgToolbar = new Array();
	
	
		var orderDlgCfg = {
			title:'订单详情',
			height:320,
			width:700,
			modal:true,
			closed:true
	};
	
	//添加订单审核按钮
	if(oper=='doReturnCheck'){
		orderDlgToolbar.push({
			text:'审核',
			iconCls:'icon-search',
			handler:doReturnCheck
		})
	}

	if(orderDlgToolbar.length>0){
		orderDlgCfg.toolbar=orderDlgToolbar;
	}
	
	$('#returnordersDlg').dialog(orderDlgCfg);
	
	
	
	var dlgTitle = "出库";
	if(type == 2){
		dlgTitle = "入库";
	}
	
	//出入库窗口
	$('#itemDlg').dialog({
		title:dlgTitle,
		width:300,
		height:200,
		modal:true,
		closed:true,
		buttons:[
		    {
		    	text:dlgTitle,
		    	iconCls:'icon-save',
		    	handler:doReturnInOutStore
		    	
		    }
		]
	});
	//出入库双击事件
	var x=oper;
	if(oper == 'doReturnInStore' || oper== 'doReturnOutStore'){
		$('#itemgrid').datagrid({
			onDblClickRow:function(rowIndex, rowData){
				$('#itemDlg').dialog('open');
				$('#id').val(rowData.uuid);
				$('#goodsuuid').html(rowData.goodsuuid);
				$('#goodsname').html(rowData.goodsname);
				$('#num').html(rowData.num);
			}
		});
	};
	
	
	if(Request['oper']== 'returnorders'){
			if(type==2){	
				$('#returnordersupplier').html("客户");
			}
		$('#grid').datagrid({
			toolbar: [{
				text: '销售退货登记',
				iconCls: 'icon-add',
				handler: function(){
					//设置保存按钮提交的方法为add
					method = "add";
					$('#returnorderForm').form('clear');
					//关闭编辑窗口
					$('#addReturnOrdersDlg').dialog('open');
				}
			}]
		
		})
		
	}
})

function getColumns(){
		return [[
		  		    {field:'uuid',title:'编号',width:100},
		  		    {field:'createtime',title:'生成日期',width:100},
		  		    {field:'checktime',title:'检查日期',width:100},
		  		    {field:'endtime',title:'结束日期',width:100},
		  		    {field:'type',title:'订单类型',width:100},
		  		    {field:'createrName',title:'下单员',width:100},
		  		    {field:'checkerName',title:'审核员',width:100},
		  		    {field:'enderName',title:'库管员',width:100},
		  		    {field:'supplierName',title:'客户',width:100},
		  		    {field:'totalmoney',title:'合计金额',width:100},
		  		    {field:'state',title:'订单状态',width:100,formatter:formatState},
		  		    {field:'waybillsn',title:'运单号',width:100},
		  		    {field: 'orders', title: '原订单号', width: 100,formatter:function(data){
		  		    	return data.uuid;
		  		    }},
					]];
	}
//明细状态
function formatDetailState(value){
	if(type == 1){
		switch(value * 1){
			case 0:return '未出库';
			case 1: return '已出库';
			default: return '';
		}
	}
	if(type == 2){
		switch(value * 1){
			case 0: return '未入库';
			case 1: return '已入库';
			default: return '';
		}
	}
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
            return '未入库';
        case 1:
            return '已入库';
        default:
            return '';
    }
}

//审核功能
function doReturnCheck(){
	$.messager.confirm('确认','确认要审核吗？',function(yes){
		
		if(yes){
			$.ajax({
				url:'returnorders_doCheck',
				dataType:'json',
				data:{id:$('#uuid').html()},
				type:'post',
				success:function(rtn){
					$.messager.alert('提示',rtn.message,'info',function(){
						if(rtn.success){
							$('#returnordersDlg').dialog("close");
							$('#grid').datagrid('reload');
						}
					})
				}
			})
		}
	})
}


//出入库
function doReturnInOutStore(){
	var message = "确认要出库吗？";
	var url = "returnorderdetail_doRetrnOutStore";
	if(type == 2){
		message = "确认要入库吗？";
		url = "returnorderdetail_doReturnInStore";
	}
	$.messager.confirm('确认',message,function(yes){
		if(yes){
			var submitData=$('#itemForm').serializeJSON();
			if(submitData.storeuuid==''){
				$.messager.alert('提示','请选择仓库','info');
				return;
			}
			
			$.ajax({
				url:url,
				dataType:'json',
				data:submitData,
				type:'post',
				success:function(rtn){
					$.messager.alert('提示',rtn.message,'info',function(){
						
						if(rtn.success){
							//关闭明细窗口
							$('#itemDlg').dialog('close');
							
							//修改明细状态
							var row=$('#itemgrid').datagrid('getSelected');
							row.state='1';
							//取出数据
							var data=$('#itemgrid').datagrid('getData');
							//加载本地数据
							$('#itemgrid').datagrid('loadData',data);
							
							var flg = true;
							$.each(data.rows,function(i,row){
								if(row.state * 1 == 0){
									flg = false;
									return false;//跳出循环
								}
							});
							
							if(flg == true){
								//关闭详情窗口
								$('#returnordersDlg').dialog('close');
								$('#grid').datagrid('reload');
							
								
							}
						}
					})
				}
				
			})
			
		}
	})
}

