<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>合同快速制作</title>
	<link href="css/bootstrap.css" rel="stylesheet">
	<link href="css/bootstrap-theme.css" rel="stylesheet">
	<link href="css/fileinput.css" rel="stylesheet">
	<link href="css/fileinput-rtl.css" rel="stylesheet">
	<script src="js/jquery-3.3.1.js"></script>
	<script src="js/bootstrap.js"></script>
	<script src="js/fileinput.js"></script>
	
</head>
<body>
<form>
	<div class="container">
		<div id="head" class="row-fluid">
			<div class="text-center" style="font-size: 70px">在线生成合同</div>
		</div>
		<div id="content" class="form-horizontal">
			<div class="col-md-12">
					<div class="form-horizontal" id="accordion-922178">
						<div class="accordion-group">
							<div class="accordion-heading">
								 <a class="accordion-toggle btn btn-info" data-toggle="collapse" data-parent="#accordion-922178" href="#accordion-element-974659">借款人信息</a>
							</div>
							<div id="accordion-element-974659" class="accordion-body in collapse">
								<div class="accordion-inner">
									<div class="table-group">
										<input id="f_upload1" type="file" class="file"/>
									</div>
								</div>
							</div>
						</div>
						<div class="accordion-group">
							<div class="accordion-heading">
								 <a class="accordion-toggle btn btn-info" data-toggle="collapse" data-parent="#accordion-922178" href="#accordion-element-522147">担保人信息</a>
							</div>
							<div id="accordion-element-522147" class="accordion-body collapse">
								<div class="accordion-inner">
									<div class="table-group">
										<input id="f_upload3" type="file" class="file"/>
									</div>
								</div>
							</div>
						</div>
						<div class="accordion-group">
							<div class="accordion-heading">
								 <a class="accordion-toggle btn btn-info" data-toggle="collapse" data-parent="#accordion-922178" href="#accordion-element-522148">偿还人信息</a>
							</div>
							<div id="accordion-element-522148" class="accordion-body collapse">
								<div class="accordion-inner">
									<div class="table-group">
										<input id="f_upload2" type="file" class="file"/>
									</div>
								</div>
							</div>
						</div>
					</div>
			</div>			
		</div>

		<div id="container" class="form-horizontal">
            <div class="form-group">
                <label for="contractId" class="col-sm-2 control-label">合同编号：</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" id = "contractId" value=${contractId} readonly>
                </div>
                <label for="borrowingBalance" class="col-sm-2 control-label">金额：</label>
                <div class="col-sm-2">
                    <input type="number" class="form-control" placeholder="请输入借款金额" id="borrowingBalance">
                </div>
                <label for="interestRate" class="col-sm-2 control-label">利率：</label>
                <div class="col-sm-2">
                    <input type="number" class="form-control" placeholder="请输入当前利率" id="interestRate">
                </div>
            </div>
        </div>
        <div id="container" class="form-horizontal">
            <div class="form-group">
                <label for="amortizationLoan" class="col-sm-2 control-label">分期：</label>
                <div class="col-sm-2">
                    <input type="number" class="form-control" placeholder="请输入分多少期还款" id="amortizationLoan">
                </div>
                <label for="accountUsername" class="col-sm-2 control-label">账户名称：</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" placeholder="请输入借款人账户名称" id="accountUsername">
                </div>
                <label for="accountNumber" class="col-sm-2 control-label">账号：</label>
                <div class="col-sm-2">
                    <input type="number" class="form-control" placeholder="请输入借款人账号" id="accountNumber">
                </div>
            </div>
		</div>
        <div id="container" class="row-fluid">
            <div class="col-md-3">

            </div>
            <div class="col-md-3">
                <select id="sel_driver" class="btn selectpicker show-tick btn-success" data-live-search="true">
                    <option value="0">请选择合同类型</option>
					<#if dataDic??>
						<#list dataDic as dic>
						<option value="${dic.dicKey}">${dic.dicValue}</option>
						</#list>
					</#if>
                </select>
            </div>
            <div class="col-md-3">
                <button type="button" id="createPdf"  class="btn btn-success" autocomplete="off">
                    生成合同
                </button>
            </div>
            <div class="col-md-3">

            </div>
        </div>

	</div>

	<div id="foot"  style="height:200px" >
		<div style="position:fixed;bottom:5px;width: 100%;">
			<marquee><font color="#FF0000">注：</font>本网站意在方便亲签署合同，生成合同后请仔细查验，本网站不承担任何法律责任,请知悉！</marquee>
		</div>
	</div>
</form>

</body>
<script>
	//借款人
    $('#f_upload1').fileinput({
        language: 'zh', //设置语言
        uploadUrl: "/uploadFile", //上传的地址
        allowedFileExtensions : ['jpg', 'png','gif'],//接收的文件后缀,
        maxFileCount: 100,
        enctype: 'multipart/form-data',
        showUpload: true, //是否显示上传按钮
        showCaption: false,//是否显示标题
        browseClass: "btn btn-primary", //按钮样式
        previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
        msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
        uploadExtraData:function(){//向后台传递参数
            var data={
                //contractType:$("#sel_driver").val(),
                contractId:$("#contractId").val(),
                personType:1
                /*versionNum:$("#versionNum").val(),
                description:$("#description").val()*/
            };
            return data;
        },
    });
    //偿还人
    $('#f_upload2').fileinput({
        language: 'zh', //设置语言
        uploadUrl: "/uploadFile", //上传的地址
        allowedFileExtensions : ['jpg', 'png','gif'],//接收的文件后缀,
        maxFileCount: 100,
        enctype: 'multipart/form-data',
        showUpload: true, //是否显示上传按钮
        showCaption: false,//是否显示标题
        browseClass: "btn btn-primary", //按钮样式
        previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
        msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
        uploadExtraData:function(){//向后台传递参数
            var data={
                //contractType:$("#sel_driver").val(),
                contractId:$("#contractId").val(),
                personType:2
                /*versionNum:$("#versionNum").val(),
                description:$("#description").val()*/
            };
            return data;
        },
    });
    //担保人
    $('#f_upload3').fileinput({
        language: 'zh', //设置语言
        uploadUrl: "/uploadFile", //上传的地址
        allowedFileExtensions : ['jpg', 'png','gif'],//接收的文件后缀,
        maxFileCount: 100,
        enctype: 'multipart/form-data',
        showUpload: true, //是否显示上传按钮
        showCaption: false,//是否显示标题
        browseClass: "btn btn-primary", //按钮样式
        previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
        msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
        uploadExtraData:function(){//向后台传递参数
            var data={
                //contractType:$("#sel_driver").val(),
                contractId:$("#contractId").val(),
				personType:3
                /*versionNum:$("#versionNum").val(),
                description:$("#description").val()*/
            };
            return data;
        },
    });

   /* $("#createPdf").*/

    $("#createPdf").click(function(){
        $.post("/createPdf",{contractId:$("#contractId").val(),contractType:$("#sel_driver").val()
            ,borrowingBalance:$("#borrowingBalance").val(),interestRate:$("#interestRate").val()
            ,amortizationLoan:$("#amortizationLoan").val(),accountUsername:$("#accountUsername").val()
            ,accountNumber:$("#accountNumber").val()},
				function (data){
			alert(data);
		});
    });
</script>
</html>