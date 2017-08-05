var existEditIndex=-1;
$(function(){
	
	$('#returnordersgrid').datagrid({
		columns:[[
		          {field:'goodsuuid',title:'商品编号',width:100,editor:{type:'numberbox',options:{
			  		  disabled:true
		  		    }}},
		          {field:'goodsname',title:'商品名称',width:100,editor:{type:'combobox',options:{
		  		    	 url:'goods_simpleList',    
			  		       valueField:'name',    
			  		       textField:'name',
			  		     onSelect:function(record){
			  		    	 var price=record.outprice;
			  		    	 var uuid=record.uuid;
			  		    	 var goodsuuidEditor =getEditor('goodsuuid');
				  		    	$(goodsuuidEditor.target).val(uuid);
				  		    	
				  		    	var priceEditor =getEditor('price');
				  		    	$(priceEditor.target).val(price);
				  		    	
				  		    	var numEditor =getEditor('num');
				  		    	$(numEditor.target).select();
				  		    	bindGridEvent();
				  		    	cal();
				  		  		sum();
			  		     }
			  		       
		          }}},
		          {field:'price',title:'价格',width:100,editor:{type:'numberbox',options:{
		  		      min:0,    
			  		  precision:2
			  		    }}},
		          {field:'num',title:'数量',width:100,editor:'numberbox'},
		          {field:'money',title:'金额',width:100,editor:{type:'numberbox',options:{
		  		      min:0,    
			  		  precision:2,
			  		  disabled:true
			  		    }}},
		          {field:'-',title:'操作',width:100,formatter:function(value,rowData,rowIndex){
		        	  if(rowData.num=='合计'){
		        		  return '';
		        	  }
		        	  return '<a href="javascript:void(0)" onclick="deleteRow('+rowIndex+')">删除</a>';
		          }}
		          ]],
        singleSelect:true,
  		showFooter:true,
  		fitColumns:true,
  		toolbar:[
  		         {iconCls: 'icon-add',
  					text:'新增',
  					handler: function(){
  						//当前存在可编辑的行，则关闭它的编辑状态 
  						if(existEditIndex>-1){
  							$('#returnordersgrid').datagrid('endEdit',existEditIndex);
  						}
  						//新增一行
  						$('#returnordersgrid').datagrid('appendRow',{num:0,money:0});
  						//获取行
  						var rows=$('#returnordersgrid').datagrid('getRows');
  						existEditIndex=rows.length-1;
  						//开启当前行的编辑状态
  						$('#returnordersgrid').datagrid('beginEdit',existEditIndex);
  						
  						bindGridEvent();
  					}
  		         },'-',
  		         {
  		        	iconCls: 'icon-save',
  					text:'提交',
  					handler: function(){
  						if(existEditIndex>-1){
  							$('#returnordersgrid').datagrid('endEdit',existEditIndex);
  						};
  						var submitData=$('#returnorderForm').serializeJSON();
  						if(submitData['t1.supplieruuid']==''){
  							$.messager.alert('提示','请选择客户','info');
  							return;
  						}
  						
  						//将商品明细做成json字符串
  						var rows=$('#returnordersgrid').datagrid('getRows');
  						submitData.json=JSON.stringify(rows);
  						$.ajax({
  							url:'returnorders_add?t1.type=2',
  							dataType:'json',
  							data:submitData,
  							type:'post',
  							success:function(rtn){
  								$.messager.alert('提示',rtn.message,'info',function(){
  									
  									$('#supplier').combogrid('clear');
  									
  									$('#returnordersgrid').datagrid('loadData',{total:0,rows:[],footer:[{num:'合计',money:0}]});
  									
  									$('#addReturnOrdersDlg').dialog('close');
  									$('#grid').datagrid('reload');
  									
  								});
  							}
  							
  						})
  					}
  		         }
  		         ],
  		       onClickRow:function(rowIndex, rowData){
  		    	   if(existEditIndex>-1){
  		    		 $('#returnordersgrid').datagrid('endEdit',existEditIndex);
  		    	   }
  		    	   existEditIndex=rowIndex;
  		    	 $('#returnordersgrid').datagrid('beginEdit',existEditIndex);
  		    	bindGridEvent();
  		       }
  		
  		
  		
	})
	
	$('#returnordersgrid').datagrid('reloadFooter',[{
      	num:'合计',
      	money:0
}]);
	
	//选择供应商的弹窗
	$('#supplier').combogrid({    
	    panelWidth:750,    
	    idField:'uuid',    
	    textField:'name',    
	    url:'supplier_list?t1.type=2',  
	    fitcolumns:true,
	    mode:'remote',
	    columns:[[    
	{field:'name',title:'名称',width:100},
	{field:'address',title:'联系地址',width:100},
	{field:'contact',title:'联系人',width:100},
	{field:'tele',title:'联系电话',width:100},
	{field:'email',title:'邮件地址',width:100}
	    ]]
	});  
})

function getEditor(_field){
	return $('#returnordersgrid').datagrid('getEditor', {index:existEditIndex,field:_field});
}

//计算商品金额
function cal(){
	var priceEditor=getEditor('price');
	var price=$(priceEditor.target).val();
	
	var numEditor=getEditor('num');
	var num=$(numEditor.target).val();
	
	var money=num * price;
	var moneyEditor=getEditor('money');
	money=money.toFixed(2);
	$(moneyEditor.target).val(money);
	
	$('#returnordersgrid').datagrid('getRows')[existEditIndex].money=money;
}

//计算总金额
function sum(){
	
	var rows=$('#returnordersgrid').datagrid('getRows');
	var totalMoney=0;
	$.each(rows,function(i,row){
		totalMoney+=parseFloat(row.money);
	})
	
	totalMoney=totalMoney.toFixed(2);
	$('#returnordersgrid').datagrid('reloadFooter',[{num:'合计',money:totalMoney}]);
}

//绑定表格按键事件，计算金额和总金额
function bindGridEvent(){
	var numEditor =getEditor('num');
  	$(numEditor.target).bind('keyup',function(){
  		cal();
  		sum();
  	});
  	numEditor =getEditor('price');
  	$(numEditor.target).bind('keyup',function(){
  		cal();
  		sum();
  	});
}

function deleteRow(index){
	$('#returnordersgrid').datagrid('endEdit',existEditIndex);
	$('#returnordersgrid').datagrid('deleteRow',index);
	var data=$('#returnordersgrid').datagrid('getData');
	$('#returnordersgrid').datagrid('loadData',data);
	sum();
}