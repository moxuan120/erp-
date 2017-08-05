var method = "";
$(function () {

    if (typeof (listParam) == 'undefined') listParam = "";
    if (typeof (saveParam) == 'undefined') saveParam = "";
    if (typeof (selectName) == 'undefined') saveParam = "";

    //初始化表格数据
    $("#grid").datagrid({
        url: name + '_list' + listParam,
        singleSelect: true,
        pagination: true,
        pageSize: 20,
        toolbar: '#tb',
        fitColumns: true,
        columns: columns
    });
    //部门双击事件显示员工
    if(name == 'dep'){
    	 $("#grid").datagrid({
    		 onDblClickRow:function(rowIndex,rowData){
    			
    			 //员工详情列表
    	    	    $('#empgrid').datagrid({
    	    	    	url:'emp_empDepList?depUuid='+rowData.uuid,
    	    	        columns: [[
    	    	            {field: 'uuid', title: '编号', width: 100},
    	    	            {field: 'name', title: '真实姓名', width: 100},
    	    	            {field: 'gender', title: '性别', width: 100,formatter:function(value){
    	    	            	if(value == 0){
    	    	            		return "女";
    	    	            	}
    	    	            	if(value == 1){
    	    	            		return "男";
    	    	            	}
    	    	            }},
    	    	            {field: 'email', title: '邮箱', width: 100},
    	    	            {field: 'tele', title: '电话', width: 100},
    	    	            {field: 'birthday', title: '生日', width: 100},
    	    	            {field: 'address', title: '地址', width: 100}
    	    	        ]],
    	    	        fitColumns: true,
    	    	        onLoadSuccess:function(data){
    	    	        	 var length = data.rows.length;
    	    	        	 if(length < 1){
    	        				 $.messager.alert("提示","当前部门没有员工","info");
    	        				 $('#empDlg').dialog('close');
    	        			 }else{
    	        				 $('#empDlg').dialog('open');
    	        			 }
    	    	        }
    	    	    });
    		 }
    	 })
    	 
    	
    }
    
    //初始化编辑窗口
    $("#editWindow").dialog({
        width: 320,
        title: '编辑',
        closed: true,
        cache: false,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-save',
            handler: submitForm
        }, {
            text: '关闭',
            iconCls: 'icon-cancel',
            handler: closeEditForm
        }]
    });

    //pagination自定义工具栏按钮
    var paginationBtn = new Array();
    paginationBtn.push({
        iconCls: 'icon-add', text: '添加', handler: function () {
            method = "add";
            $("#editForm").form('clear');
            $("#editWindow").window('open');
        }
    });
    if(name=='supplier'||name=='goods'||name=='emp'){
        paginationBtn.push('-', {
            iconCls: 'icon-excel',
            text: '导出',
            handler: function () {
                var data = $("#searchForm").serializeJSON();
                $.download(name + "_export" + listParam, data);
            }
        }, '-', {
            iconCls: 'icon-excel',
            text: '下载模板',
            handler: function () {
                $.download(name + "_exportModel",{'t1.type':Request['type']});
            }
        }, '-', {
            iconCls: 'icon-save',
            text: '导入',
            handler: function () {
                $('#importDlg').dialog("open");
            }
        })
    }
    $('#grid').datagrid('getPager').pagination({
        buttons:paginationBtn
    });
    $('#importDlg').dialog({
        title:'导入数据',
        height:102,
        width:330,
        closed:true,
        modal:true,
        buttons:[{
            text:'导入',
            iconCls:'icon-save',
            handler:function(){
                $.ajax({
                    url:name+'_doImport',
                    data: new FormData($('#importForm')[0]),
                    dataType: 'json',
                    type: 'post',
                    processData:false,
                    contentType:false,
                    success:function(rtn){
                        $.messager.alert("提示",rtn.message,'info',function(){
                            if(rtn.success){
                                $('#importDlg').dialog('close');
                                $('#grid').datagrid('reload');
                            }
                        });
                    }
                });
            }
        }]
    });
    if (typeof (selectName) != 'undefined') {//simpleList
        $.post(selectName + '_simpleList', function (data) {
            var simpleList = [{"name": "未定义", "uuid": ""}];
            for (var attr in data) {
                simpleList[attr * 1 + 1] = data[attr];
            }
            $("#select1").combobox({
                data: simpleList
            });
            $("#select2").combobox({
                data: simpleList
            });
        }, 'json');
    }

});

function del(uuid) {
    $.messager.confirm('确认', '您确认要删除吗？', function (yes) {
        if (yes) {
            $.post(name + "_del", {"id": uuid}, function (data) {
                $.messager.alert('提示', data.message, 'info', function () {
                    if (data.success) {
                        $("#grid").datagrid('reload');
                    }
                });
            }, "json");
        }
    })
}

function refresh() {
    var data = $("#searchForm").serializeJSON();
    $("#grid").datagrid('load', data)
}

function edit(uuid) {
    method = "update";
    $("#editWindow").window('open');
    $('#editForm').form('load', name + '_get?id=' + uuid);
}


function submitForm() {
    $('#editForm').form('submit', {
        url: name + '_' + method + saveParam,
        onSubmit: function () {
            return $(this).form('enableValidation').form('validate');
        },
        success: function (data) {
            var data = eval('(' + data + ')'); //json转化成js对象
            $.messager.alert('提示', data.message, 'info', function () {
                if (data.success) {
                    $("#grid").datagrid('reload');
                    $("#editWindow").window('close');
                }
            });
        }
    });
}

function closeEditForm() {
    $("#editWindow").window('close');
}

function clearSearchForm() {
    $("#searchForm").form('clear');
}