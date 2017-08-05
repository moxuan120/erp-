var oper = Request['oper'];
var type = Request['type'];
var id;
$(function() {
	$('#addReturnOrdersDlg').dialog({
		buttons: [{
            text: '保存',
            iconCls: 'icon-save',
            handler: function() {
            	$.messager.confirm('确认', '确定要退货吗？', function (yes) {
			        if (yes) {
			        	var num = $("#returnNum").val();
			        	if(num <1) {
                            $.messager.alert('提示', "请选择数量", 'info');
                            return;
						}
			        	$.post('returnorders_addDetail',{id:id,num:num,type:type},function(data){
			        		$.messager.alert('提示', data.message, 'info', function () {
			                    if (data.success) {
			                        //刷新表格
			                    	$('#itemgrid').datagrid('reload');
			                        $('#grid').datagrid('reload');
                                    $('#addReturnOrdersDlg').dialog('close');
                                    $('#ordersDlg').dialog('close');
			                    }
			                });
			        	},'json');
			        }
			    });
			}
        }, {
            text: '关闭',
            iconCls: 'icon-cancel',
            handler: function() {
            	$('#returnorderForm').form('clear');
				//关闭编辑窗口
				$('#addReturnOrdersDlg').dialog('close');
			}
        }]
	})
	
	if (oper == 'doReturn') {
		$('#itemgrid').datagrid({
			toolbar: [{
				text: '整单退货申请',
				iconCls: 'icon-add',
				handler: function(){
					$.messager.confirm('确认', '确定要退货吗？', function (yes) {
				        if (yes) {
				            $.post('returnorders_add', {id: $('#uuid').html(),type:type}, function (data) {
				                $.messager.alert('提示', data.message, 'info', function () {
				                    if (data.success) {
				                        //刷新表格
				                    	$('#itemgrid').datagrid('reload');
				                        $('#grid').datagrid('reload');
                                        $('#ordersDlg').dialog('close');
				                    }
				                });
				            }, 'json');
				        }
				    });
				}
			}],
			onDblClickRow:function(rowIndex,rowData){
				id = rowData.uuid;
                $('#returnName').html(rowData.goodsname);
                $('#ordersNum').html(rowData.num);
				//关闭编辑窗口
				$('#addReturnOrdersDlg').dialog('open');
			}
		
		})
	}
})