// 字符最小长度验证（一个中文字符长度为2）
jQuery.validator.addMethod("stringMinLength", function(value, element, param) {
	var length = value.length;
	for ( var i = 0; i < value.length; i++) {
		if (value.charCodeAt(i) > 127) {
			length++;
		}
	}
	return this.optional(element) || (length >= param);
}, $.validator.format("长度不能小于{0}！注：一个中文长度为2"));
// 字符最大长度验证（一个中文字符长度为2）
jQuery.validator.addMethod("stringMaxLength", function(value, element, param) {
	var length = value.length;
	for ( var i = 0; i < value.length; i++) {
		if (value.charCodeAt(i) > 127) {
			length++;
		}
	}
	return this.optional(element) || (length <= param);
}, $.validator.format("长度不能大于{0}！注：一个中文长度为2"));
// 字母数字 
jQuery.validator.addMethod("alnum", function (value, element) {
    return this.optional(element) || isEnglishOrNumber(value);
}, "只能包括英文字母和数字");

//字母或中文 
jQuery.validator.addMethod("isEnglishOrChinese", function (value, element) {
  return this.optional(element) || isEnglishOrChinese(value);
}, "只能包括英文字母或中文");

// 电话号码验证
jQuery.validator.addMethod("isTel", function (value, element) {
    var tel = /^\d{3,4}-?\d{7,8}$/;    // 电话号码格式010-12345678
    return this.optional(element) || (tel.test(value));
}, "请您正确填写电话号码");

// 邮政编码验证
jQuery.validator.addMethod("isZipCode", function (value, element) {
    var tel = /^[0-9]{6}$/;
    return this.optional(element) || (tel.test(value));
}, "请您正确填写邮政编码");

//年龄验证
jQuery.validator.addMethod("isAge", function (value, element) {
    var tel = /^(110|10\d|[1-9]\d|\d)$/;
    return this.optional(element) || (tel.test(value));
}, "请您正确年龄");

// 身份证号码验证（加强验证）
jQuery.validator.addMethod("isIdCardNo", function (value, element) {
	var checkResult = (checkIdcard(value)==true);
    return this.optional(element) || checkResult;
}, "请您正确填写身份证号码");

// QQ号码验证
jQuery.validator.addMethod("isQQ", function (value, element) {
    var tel = /^\d{5,13}$/;
    return this.optional(element) || (tel.test(value));
}, "请您正确填写QQ号码");
//校验Ip
jQuery.validator.addMethod("isIp", function(value, element) {
	var ip = /^([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])$/;
	return this.optional(element) || (ip.test(value));
},"请您正确填写IP地址");
//判断为Url
jQuery.validator.addMethod("isUrl", function (value, element) {
	var strRegex = "^((https|http):\/\/)?"  
					+ "(((([0-9]|1[0-9]{2}|[1-9][0-9]|2[0-4][0-9]|25[0-5])[.]{1}){3}([0-9]|1[0-9]{2}|[1-9][0-9]|2[0-4][0-9]|25[0-5]))" // IP>形式的URL- 199.194.52.184  
					+ "|"  
					+ "([0-9a-zA-Z\u4E00-\u9FA5\uF900-\uFA2D-]+[.]{1})+[a-zA-Z-]+)" // DOMAIN（域名）形式的URL  
					+ "(:[0-9]{1,4})?" // 端口- :80  
					+ "((/?)|(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?){1}$";  

	var re=new RegExp(strRegex); 
	return this.optional(element) || (re.test(value));
}, "请您正确填写url地址!");

//验证是否为数字
jQuery.validator.addMethod("isNum", function (value, element) {
    var tel = /^\d{3,13}$/;
    return this.optional(element) || (tel.test(value));
}, "数据格式错误！");

//验证是否为数字
jQuery.validator.addMethod("isNumber", function (value, element) {
    var tel = /^\d{1,12}$/;
    return this.optional(element) || (tel.test(value));
}, "数据格式错误！");

//验证是否为整型数字
jQuery.validator.addMethod("isInteger", function (value, element) {
    var tel = /^\d{1,9}$/;
    return this.optional(element) || (tel.test(value));
}, "请您填写数字！");

//验证是否为数字或2位小数
jQuery.validator.addMethod("digital", function(value, element) {
	var tel = /^\d+(\.\d{1,2})?$/;
	return this.optional(element) || (tel.test(value));
}, "数据格式错误！");

//验证是否为数字或4位小数
jQuery.validator.addMethod("decimal", function(value, element) {
	var tel = /^\d+(\.\d{1,4})?$/;
	return this.optional(element) || (tel.test(value));
}, "数据格式错误！");

//验证是否为正数
jQuery.validator.addMethod("positive", function(value, element) {
	var tel = /^[1-9]\d*$/;
	return this.optional(element) || (tel.test(value));
}, "数据格式错误！");

//验证是否为图片路径
jQuery.validator.addMethod("imageTest", function(value, element) {
	return (this.optional(element) || value.toLowerCase().indexOf(".jpg")  > 0 || value.toLowerCase().indexOf(".gif") >0 || 
			value.toLowerCase().indexOf(".jpeg") >0 || value.toLowerCase().indexOf(".png")  > 0);
}, "非正确的图片文件！");

//验证只能为英文或中文或数字或_
jQuery.validator.addMethod("isEnglishOrNumberOrChinese", function(value, element){
	  return this.optional(element) || isEnglishOrNumberOrChinese(value);
}, "不要输入特殊字符!");

//验证:只能为英文,数字或_
jQuery.validator.addMethod("isEnglishOrNumberOr_", function(value, element) {
	return this.optional(element) || (isEnglishOrNumberOr_(value));
}, "只能为字母或数字或下划线！");

/**
 * @功能:验证是否为正数
 * @param str
 * @returns
 */
function isPositive(str){
	return /^[1-9]\d*$/.test(str);
}

/**
 * @功能:校验是否包含中文
 */
function isHaveChinese(str){
	return /.*[\u4e00-\u9fa5]+.*$/.test(str);
}

/**
 * @功能:是否为正确的邮箱
 * @param email
 * @returns {Boolean}
 */
function isEmail(email){
	var mailReg_normal = new RegExp(/^[A-Za-z0-9][\w+|\-\.]{2,}@(?:[a-z0-9](?:[-]?[a-z0-9]+)*\.){1,3}(?:com|org|edu|net|gov|cn|hk|tw|jp|de|uk|fr|mo|eu|sg|kr)$/);
	if(mailReg_normal.test(email)){
		return true;
	}
	return false;
}

/**
 * @功能:电话号码校验
 * @param str
 * @returns {___anonymous4329_4357}
 */
function isMobileNumber(str){
	return /^\d{11}$/.test(str);
}

/**
 * @功能:只能为中文和英文
 */
function isEnglishOrChinese(str){
	return /^[A-Za-z\u4e00-\u9fa5]+$/.test(str);
}

/**
 * @功能:只能为中文和英文和反斜杠
 */
function isEnglishOrChineseOrBackslash(str){
	return /^[A-Za-z\u4e00-\u9fa5\/]+$/.test(str);
}

/**
 * @功能:只能是英文和数字
 */
function isEnglishOrNumber(str){
	return /^[A-Za-z0-9]+$/.test(str);
}

/**
 * @功能:只能为英文或中文或数字或_
 */
function isEnglishOrNumberOrChinese(str){
	return /^[A-Za-z0-9_\u4e00-\u9fa5]+$/.test(str);
}

/**
 * @功能:只能为英文或数字或_
 */
function isEnglishOrNumberOr_(str){
	return /^[A-Za-z0-9_]+$/.test(str);
}

/**
 * @功能:非空校验
 * @param str
 * @returns {___anonymous8117_8145}
 */
function isEmptyVal(str){
	if(str == null || str == "" ) {
		return true;
	}
	return false;
}

/**
 * 身份证15位编码规则：dddddd yymmdd xx p dddddd：地区码 yymmdd: 出生年月日 xx: 顺序类编码，无法确定 p:
 * 性别，奇数为男，偶数为女
 * <p />
 * 身份证18位编码规则：dddddd yyyymmdd xxx y dddddd：地区码 yyyymmdd: 出生年月日
 * xxx:顺序类编码，无法确定，奇数为男，偶数为女 y: 校验码，该位数值可通过前17位计算获得
 * <p />
 * 18位号码加权因子为(从右到左) Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2,1 ]
 * 验证位 Y = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ] 校验位计算公式：Y_P = mod( ∑(Ai×Wi),11 )
 * i为身份证号码从右往左数的 2...18 位; Y_P为脚丫校验码所在校验码数组位置
 * 
 */

function checkParseIdCard(val) {
	var val = trim(val);
	var msg = checkIdcard(val);
	if (msg != "验证通过!") {
		return;
	}

	var birthdayValue;
	var sexId;
	var sexText;
	var age;

	if (15 == val.length) { // 15位身份证号码
		birthdayValue = val.charAt(6) + val.charAt(7);
		if (parseInt(birthdayValue) < 10) {
			birthdayValue = '20' + birthdayValue;
		} else {
			birthdayValue = '19' + birthdayValue;
		}
		birthdayValue = birthdayValue + '-' + val.charAt(8) + val.charAt(9)
				+ '-' + val.charAt(10) + val.charAt(11);
		if (parseInt(val.charAt(14) / 2) * 2 != val.charAt(14)) {
			sexId = "1";
			sexText = "男";
		} else {
			sexId = "2";
			sexText = "女";
		}
	}
	if (18 == val.length) { // 18位身份证号码
		birthdayValue = val.charAt(6) + val.charAt(7) + val.charAt(8)
				+ val.charAt(9) + '-' + val.charAt(10) + val.charAt(11) + '-'
				+ val.charAt(12) + val.charAt(13);
		if (parseInt(val.charAt(16) / 2) * 2 != val.charAt(16)) {
			sexId = "1";
			sexText = "男";
		} else {
			sexId = "2";
			sexText = "女";
		}
	}
	// 年龄
	var dt1 = new Date(birthdayValue.replace("-", "/"));
	var dt2 = new Date();
	var age = dt2.getFullYear() - dt1.getFullYear();
	var m = dt2.getMonth() - dt1.getMonth();
	if (m < 0)
		age--;
	alert(birthdayValue + sexId + sexText + age);
}

/**
 * @功能:校验身份证的合法性
 * @param idcard
 * @returns
 */
function checkIdcard(idcard) {
	var Errors = new Array(true, "身份证号码位数不对。", "身份证号码出生日期超出范围或含有非法字符。",
			"身份证号码错误，请核对并修改。", "身份证地区非法。");
	var area={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",
	  23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",
	  41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",
	  52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",
	  71:"台湾",81:"香港",82:"澳门",91:"国外"} 
	var idcard, Y, JYM;
	var S, M;
	var idcard_array = new Array();
	idcard_array = idcard.split("");
	// 地区检验
	if (area[parseInt(idcard.substr(0, 2))] == null)
		return Errors[4];
	// 身份号码位数及格式检验
	switch (idcard.length) {
	case 15:
		if ((parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0
				|| ((parseInt(idcard.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(idcard
						.substr(6, 2)) + 1900) % 4 == 0)) {
			ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/; // 测试出生日期的合法性
		} else {
			ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/; // 测试出生日期的合法性
		}
		if (ereg.test(idcard))
			return Errors[0];
		else
			return Errors[2];
		break;
	case 18:
		// 18位身份号码检测
		// 出生日期的合法性检查
		// 闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
		// 平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
		if (parseInt(idcard.substr(6, 4)) % 4 == 0
				|| (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard
						.substr(6, 4)) % 4 == 0)) {
			ereg = /^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/; // 闰年出生日期的合法性正则表达式
		} else {
			ereg = /^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/; // 平年出生日期的合法性正则表达式
		}
		if (ereg.test(idcard)) {// 测试出生日期的合法性
			// 计算校验位
			S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
					+ (parseInt(idcard_array[1]) + parseInt(idcard_array[11]))* 9
					+ (parseInt(idcard_array[2]) + parseInt(idcard_array[12]))* 10
					+ (parseInt(idcard_array[3]) + parseInt(idcard_array[13]))* 5
					+ (parseInt(idcard_array[4]) + parseInt(idcard_array[14]))* 8
					+ (parseInt(idcard_array[5]) + parseInt(idcard_array[15]))* 4
					+ (parseInt(idcard_array[6]) + parseInt(idcard_array[16]))* 2
					+ parseInt(idcard_array[7]) * 1
					+ parseInt(idcard_array[8]) * 6 
					+ parseInt(idcard_array[9]) * 3;
			Y = S % 11;
			M = "F";
			JYM = "10X98765432";
			M = JYM.substr(Y, 1); // 判断校验位
			if (M == idcard_array[17].toUpperCase())
				return Errors[0]; // 检测ID的校验位
			else
				return Errors[3];
		} else
			return Errors[2];
		break;
	default:
		return Errors[1];
		break;
	}
}

 /**
	 * 验证18位数身份证号码中的生日是否是有效生日
	 * 
	 * @param idCard
	 *            18位书身份证字符串
	 * @return
	 */  
function isValidityBrithBy18IdCard(idCard18){   
    var year =  idCard18.substring(6,10);   
    var month = idCard18.substring(10,12);   
    var day = idCard18.substring(12,14);   
    var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));   
    // 这里用getFullYear()获取年份，避免千年虫问题
    if(temp_date.getFullYear()!=parseFloat(year)   
          ||temp_date.getMonth()!=parseFloat(month)-1   
          ||temp_date.getDate()!=parseFloat(day)){   
            return false;   
    }else{   
        return true;   
    }   
}  

/**
 * 验证15位数身份证号码中的生日是否是有效生日
 * 
 * @param idCard15
 *            15位书身份证字符串
 * @return
 */  
function isValidityBrithBy15IdCard(idCard15){   
	var year =  idCard15.substring(6,8);   
	var month = idCard15.substring(8,10);   
	var day = idCard15.substring(10,12);   
	var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));   
	// 对于老身份证中的你年龄则不需考虑千年虫问题而使用getYear()方法
	if(temp_date.getYear()!=parseFloat(year)   
			||temp_date.getMonth()!=parseFloat(month)-1   
			||temp_date.getDate()!=parseFloat(day)){   
    	  return false;   
	}else{   
		return true;   
	}   
}   

/**
 * 跨站脚本攻击
 */
function checkInput(){
	$("input[type='text']").each(function(i){
	 	var str = this.value;
	 	var regc = /<|>|(script)|on(mouseover|mouseon|mouseout|click|dblclick|blur|focus|change)|\t/
		var rs = regc.exec(str);
		if(rs!=null){
			this.value = "";
			return;
		}
	});
	
	//验证是否为图片路径
	jQuery.validator.addMethod("imageTest", function(value, element) {
		return (this.optional(element) || value.toLowerCase().indexOf(".jpg")  > 0 || value.toLowerCase().indexOf(".gif") >0 || 
				value.toLowerCase().indexOf(".jpeg") >0 || value.toLowerCase().indexOf(".png")  > 0);
	}, "非正确的图片文件！");

}
