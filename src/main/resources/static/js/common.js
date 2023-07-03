
// 숫자
function onlyNumeric(e) {
    var e 			= e || window.event
        ,keyCode 	= (e.which) ? e.which : e.keyCode;

    if (keyCode == 8 || keyCode == 46 || keyCode == 37 || keyCode == 39) {
        return;
    } else {
        e.target.value = e.target.value.replace(/[^0-9]/g, '');
    }
}



/**
 * Open Aavatar Utils
 */
var common = common || {};
common = (function () {
    'use strict';

    return {
        // 문자열
        string: {
            /**
             * 문자가 빈문자열일 경우 null을 반환한다.
             * @param str
             * @returns {null|*}
             */
            nvl : function (str){
                if(_.isEmpty(str)) return null;
                return str;
            },
            /**
             * 문자가 빈문자열일 경우 지정문자로 반환한다.
             * @param str 문자
             * @param str2 지정문자
             * @returns {*}
             */
            nvl2 : function (str, str2){
                if(_.isEmpty(str)) return str2;
                return str;
            },
            /**
             * oau.string.format('{0}입니다.', '사과') => 사과입니다.
             * @param format 포멧
             * @param args 파라미터
             * @returns 포멧팅문자열
             */
            format: function (format, ...args){
                return format.replace(/{(\d+)}/g, function(match, num) {
                    num = Number(num);
                    return typeof(args[num]) != undefined ? args[num] : match;
                });
            },
            /**
             * 문자열 이스케이프
             */
            htmlEscape:function(str) {
                return str
                    .replace(/&/g, '&amp;')
                    .replace(/"/g, '&quot;')
                    .replace(/'/g, '&#39;')
                    .replace(/</g, '&lt;')
                    .replace(/\//g, '&#92;')
                    .replace(/>/g, '&gt;');
            },
            /**
             * 문자열 언이스케이프
             */
            htmlUnescape:function(str) {
                return str
                    .replace(/&quot;/g, '"')
                    .replace(/&#39;/g, "'")
                    .replace(/&#x27;/g, "'")
                    .replace(/&lt;/g, '<')
                    .replace(/&gt;/g, '>')
                    .replace(/&#92;/g, '/')
                    .replace(/&#x2F;/g, '/')
                    .replace(/&amp;/g, '&')
                    .replace(/&#034;/g, '"');
            },
            /**
             * Url인코딩된 문자열인지 확인
             */
            isEncoded: function(str) {
                str = str || '';
                return str !== decodeURIComponent(str);
            },
        },
        // 파일
        file: {
            // 임시파일 업로드
            upload: function (opts) {
                var slef = this;
                var formData = new FormData();
                formData.append("uploadFile", document.querySelector(opts.el).files[0]);
                common.ajax._submit("/common/file/upload", formData, opts.callback, opts.error);
            },
            // 다중 임시파일 업로드
            multiUpload: function (opts) {
                var slef = this;
                var formData = new FormData();
                var exsitFiles = false;

                if(!_.isArray(opts.el)) throw new Error("el must be an array");

                _.forEach(opts.el, function(v) {
                    let file = document.querySelector(v).files[0]
                    if(file){
                        formData.append("lables", common.string.nvl2($(v).data("label"), ""));
                        formData.append("uploadFiles", document.querySelector(v).files[0]);
                    }
                });

                if( formData.has("uploadFiles")){
                    common.ajax._submit("/common/file/multi-upload", formData, opts.callback, opts.error);
                }else{
                    if(opts.callback) opts.callback([]);
                }
            },
            // 다운로드
            download: function (opts) {

                if (!opts.url) opts.url = "/common/file/download";

                $.fileDownload(opts.url, {
                    httpMethod: "GET"
                    , data: opts.params
                    , prepareCallback: function () {
                    }
                    , successCallback: function (data) {
                        if( opts.successCallback) opts.successCallback();
                    }
                    , failCallback: function (responseHtml, url, error) {
                        var o = JSON.parse(responseHtml.substring(responseHtml.indexOf("{"), responseHtml.lastIndexOf("}") + 1));
                        alert(o.message);
                    }
                });
            }
        },
        // 엑셀
        excel: {
            // 업로드
            upload: function (opts) {
                var slef = this;
                var formData = new FormData();
                formData.append("uploadFile", document.querySelector(opts.el).files[0]);
                formData.append("serviceName", opts.serviceName);
                formData.append("uid", opts.uid);

                if (opts.params) {
                    for (var key of Object.keys(opts.params)) {
                        formData.append("[__" + key + "__]", opts.params[key]);
                    }
                }

                common.ajax._submit("/excel/upload", formData, opts.callback, opts.error);
            },
            // 엑셀 다운로드
            download: function (opts) {
                var params = {};

                params["serviceName"] = opts.serviceName;
                params["templateName"] = opts.templateName;
                params["saveName"] = opts.saveName;
                params["uid"] = opts.uid;

                if (opts.params) {
                    for (var key of Object.keys(opts.params)) {
                        params["[__" + key + "__]"] = opts.params[key];
                    }
                }

                common.file.download({
                    url: "/excel/download"
                    , params: params
                    , successCallback : opts.callback
                    , failCallback: opts.error
                })

            },
            // 양식
            form: {
                // 양식 다운로드
                download: function (opts) {

                    var params = {};

                    params["serviceName"] = opts.serviceName;
                    params["templateName"] = opts.templateName;
                    params["saveName"] = opts.saveName;
                    params["uid"] = opts.uid;

                    if (opts.params) {
                        for (var key of Object.keys(opts.params)) {
                            params["[__" + key + "__]"] = opts.params[key];
                        }
                    }

                    common.file.download({
                        url: "/excel/download-form"
                        , params: params
                    })
                }
            }
        },
        // 비동기 데이터 통신
        ajax: {
            // Form 요청
            submit: function (url, el, success, fail) {
                var slef = this;
                var formData = new FormData($(el)[0]);
                this._submit(url, formData, success, fail);
            },
            _submit: function (url, formData, success, fail) {
                var slef = this;

                this.post({
                    url: url,
                    data: formData,
                    cache: false,
                    contentType: false,
                    processData: false,
                    success: function (data) {
                        if (success) success(data);
                    },
                    error: function (xhr, status, error) {

                        if( xhr.status == 401 || xhr.status == 403) {
                            window.location.href = "/login";
                            return;
                        }

                        var result = JSON.parse(xhr.responseText)
                        if(fail){
                            fail(result)
                        }else{
                            alert(result.message);
                        }
                    }
                })
            },
            // Call
            call: function (opts){

                if(!opts.url) throw new Error("url은 필수값입니다.")
                if(!opts.method) throw new Error("method는 필수값입니다.")

                let successCallback = opts.success;
                let errorCallback = opts.error;

                var defaultOpts = {
                    async: opts.async !== undefined ? opts.async : false
                    ,beforeSend: function( xhr ) {
                        let csrf = $("meta[name='csrf']").attr('content');
                        xhr.setRequestHeader("X-CSRF-TOKEN", csrf);
                    }
                    ,success : function (data){
                        if(successCallback){
                            successCallback(data);
                        } else {
                            console.log(data);
                        }
                    }
                    ,error: function(xhr, status, error){

                        if( xhr.status == 401 || xhr.status == 403) {
                            window.location.href = "/login";
                            return;
                        }

                        if( errorCallback){
                            errorCallback(xhr, status, error);
                        }else{
                            var result = JSON.parse(xhr.responseText)
                            alert(result.message);
                        }
                    }
                }

                delete opts['success'];
                delete opts['error'];

                opts  = _.extend(defaultOpts, opts)

                $.ajax(opts);
            },
            // Get 요청
            get: function (opts) {
                var defaultOpts = {
                    method: "GET"
                }
                if( opts.url && opts.url.indexOf("?") != -1){
                    var u = opts.url.split("?")
                    var b = u[0];
                    var q = u[1];
                    var nq = []
                    if(q){
                        q = q.split("&")
                        _.each(q, function (value) {
                            var t = value.split("=");
                            var k = t[0];
                            var v = common.string.isEncoded(t[1]) ? t[1] : encodeURIComponent(t[1])
                            nq.push(k + "=" + v)
                        })
                        opts.url = b + "?" + nq.join("&");
                    }
                }
                opts  = _.extend(defaultOpts, opts)
                this.call(opts)
            },
            // Post 요청
            post: function (opts) {
                var defaultOpts = {
                    method: "POST"
                }
                opts  = _.extend(defaultOpts, opts)
                this.call(opts)
            }
        }
    }

}());
