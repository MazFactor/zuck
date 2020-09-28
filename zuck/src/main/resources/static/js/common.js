/**
 * Created by liao on 2017/9/3.
 */
function text_color () {
  $ (".text_color2").highlight (query_str, {insensitive: 1});
}

function full_view (title, url) {
  var index = layer.open ({
    type: 2,
    title: title,
    content: url,
    area: [ '800px', '500px' ],
    btn: [ '刷新', '全屏', '平板', '手机' ],
    yes: function (index, layero) {
      layer.iframeSrc (index, url);
      return false;
    },
    btn2: function (index, layero) {
      layer.full (index);
      layer.iframeSrc (index, url);
      return false;
      //按钮【按钮一】的回调
    }
    , btn3: function (index, layero) {
      //按钮【按钮二】的回调
      layer.style (index, {
        width: '621px',
        height: '755px'
      });
      layer.iframeSrc (index, url);
      layer.iframeAuto (index);

      return false;
      //return false 开启该代码可禁止点击该按钮关闭
    }
    , btn4: function (index, layero) {
      //按钮【按钮三】的回调
      layer.style (index, {
        width: '375px',
        height: '667px'
      });
      layer.iframeSrc (index, url);
      layer.iframeAuto (index);

      return false;
      //return false 开启该代码可禁止点击该按钮关闭
    }
    , cancel: function () {
      //右上角关闭回调
      //return false 开启该代码可禁止点击该按钮关闭
    }
    // maxmin: true
  });
  return false;
}

// 提示确认框
function confirmfun (title, url, text) {
  text = text == undefined ? '是否操作？' : text;
  top.layer.confirm (text, {icon: 3, title: title}, function (index) {
    $.get (url, function (data) {
      if (data.status == 1) {
        top.layer.msg (data.info, {icon: 1, time: 2000, shade: 0.3});
        setTimeout ("getDataList()", 2000);
      } else {
        top.layer.msg (data.info, {icon: 2, time: 2000, shade: 0.3});
      }
    })
  });
}

$ (function () {
  $ (".upload-box").on ('click', '.del', function () {
    $ (this).parent ('.upload-pre-file,.upload-pre-image').remove ();
  });
  //签到
  $ ("#btn_sign_in").click (function () {

    if ($ ("#btn_sign_in").data ('sign') == 1) {
      top.layer.msg ('亲，今天已签到', {icon: 2, time: 1500, shade: 0.3});
      return false;
    }
    var url = $ ("#btn_sign_in").data ('url');
    if (url == '') {
      top.layer.msg ('暂时无法签到', {icon: 2, time: 1500, shade: 0.3});
      return false;
    }
    $.ajax ({
      type: "post",
      url: url,
      data: '',
      complete: function () {
        $ ("#submit-form-btn").removeClass ('disabled').prop ('disabled', false);
      },
      error: function () {
        top.layer.msg ('网络访问失败', {icon: 2, time: 1500, shade: 0.3});
      },
      success: function (data) {
        if (data.status == 1) {
          top.layer.msg (data.info, {icon: 6, time: 1500, shade: 0.3});
          $ ("#btn_sign_in").text ('今天已签到');
          $ ("#btn_sign_in").data ('sign', '1');
          $ ("#btn_sign_in").addClass ('disabled');
          get_sign_in ();//刷新已签到标识
        } else {
          top.layer.msg (data.info, {icon: 2, time: 1500, shade: 0.3});
        }
      }
    })
  });
//查看签到排行榜
  $ ("#signInTop").click (function () {
    var loadIndex = layer.load (1, {shade: 0.5});
    $.post (LIAO.BASE_URL + '/get_active_sign_in', function (data) {
      if (data.status == 1) {
        var html = '';
        $.each (data.signList, function (ind, vals) {
          var show = ind === 0 ? 'layui-show' : '';
          html += '<ul class="layui-tab-item ' + show + '">';
          if (vals.length > 0) {
            $.each (vals, function (ind2, val) {
              if (ind === 0 || ind === 1) {
                html += '<li>\n' +
                  '                    <img src="' + val.avatar + '"><cite>' + val.nickname + '</cite>\n' +
                  '                <span>签到于 ' + val.sign_time + '</span>\n' +
                  '            </li>';
              } else if (ind === 2) {
                html += '<li>\n' +
                  '                    <img src="' + val.avatar + '"><cite>' + val.nickname + '</cite>\n' +
                  '                <span>已连续签到 <i>' + val.last_number + '</i> 天</span>\n' +
                  '            </li>';
              }
            });
          } else {
            html += '<li><span>暂无记录...</span></li>';
          }
          html += '</ul>'
        });
        var content = '<div class="layui-tab layui-tab-brief" style="margin: 5px 0 0;">\n' +
          '    <ul class="layui-tab-title">\n' +
          '        <li class="layui-this">最新签到</li>\n' +
          '        <li>今日最快</li>\n' +
          '        <li>总签到榜</li>\n' +
          '    </ul>\n' +
          '    <div class="layui-tab-content active-sign-in-list" id="LAY_signin_list">' +
          html +
          '</div>' +
          '</div>';
        layer.open ({
          type: 1
          , title: '签到活跃榜 - TOP 20'
          , area: '300px'
          , shade: 0.6
          , shadeClose: true
          , id: 'layer-pop-signintop'
          , content: content
        });
        layer.close (loadIndex)
      }
    });
  });
  //本站积分
  $ ("#integralListWindow").click (function () {
    layer.open ({
      type: 2
      , title: '本站积分 - 数据公开'
      , area: [ '60%', '50%' ]
      , shade: 0.6
      , shadeClose: true
      , scrollbar: false
      , id: 'layer-pop-signintop'
      , content: LIAO.BASE_URL + '/integral_list'
    });
  });
  //捐赠墙
  $ ('#donate-list').click (function () {
    layer.open ({
      type: 2
      , title: '捐赠墙'
      , area: [ '50%', '50%' ]
      , shade: 0.6
      , shadeClose: true
      , scrollbar: false
      , id: 'layer-pop-signintop'
      , content: LIAO.BASE_URL + '/donate_list'
    });
  });
});


// Ajax post提交
$ ("#submit-form-btn").click (function () {
  if ($ (this).attr ('confirm') != '' && $ (this).attr ('confirm') != undefined) {
    var confirmText = $ (this).attr ('confirm');
    top.layer.confirm (confirmText, {icon: 3, title: '确认'}, function (index) {
      $ ("#submit-form-btn").addClass ('disabled').prop ('disabled', true);
      var query = $ ("#form-iframe-add").serialize ();
      var url = $ ("#form-iframe-add").attr ('action');
      $.ajax ({
        type: "post",
        url: url,
        data: query,
        complete: function () {
          $ ("#submit-form-btn").removeClass ('disabled').prop ('disabled', false);
        },
        error: function () {
          top.layer.msg ('访问失败', {icon: 2, time: 1500, shade: 0.3});
        },
        success: function (data) {
          if (data.status == 1) {
            top.layer.msg (data.info, {icon: 6, time: 1500, shade: 0.3});
            if (data.url) {
              setTimeout (function () {
                parent.location.href = data.url;
              }, 1500);
            } else {
              setTimeout (function () {
                var index = parent.layer.getFrameIndex (window.name); //先得到当前iframe层的索引
                parent.layer.close (index); //再执行关闭
                // location.reload();
              }, 1500);
            }
          } else {
            top.layer.msg (data.info, {icon: 5, time: 1500, shade: 0.3});
          }
          $ ("#submit-form-btn").removeClass ('disabled').prop ('disabled', false);
        }
      })
      top.layer.close (index);
    });
    return false;
  }
  $ ("#submit-form-btn").addClass ('disabled').prop ('disabled', true);
  var query = $ ("#form-iframe-add").serialize ();
  var url = $ ("#form-iframe-add").attr ('action');
  $.ajax ({
    type: "post",
    url: url,
    data: query,
    complete: function () {
      $ ("#submit-form-btn").removeClass ('disabled').prop ('disabled', false);
    },
    error: function () {
      top.layer.msg ('访问失败', {icon: 2, time: 1500, shade: 0.3});
    },
    success: function (data) {
      if (data.status == 1) {
        top.layer.msg (data.info, {icon: 6, time: 1500, shade: 0.3});
        if (data.url != undefined && data.url != '') {
          setTimeout (function () {
            parent.location.href = data.url;
          }, 1500);
        } else {
          setTimeout (function () {
            var index = parent.layer.getFrameIndex (window.name); //先得到当前iframe层的索引
            parent.layer.close (index); //再执行关闭
            // location.reload();
          }, 1500);
        }
      } else {
        top.layer.msg (data.info, {icon: 5, time: 1500, shade: 0.3});
      }
      $ ("#submit-form-btn").removeClass ('disabled').prop ('disabled', false);
    }
  })
});

function get_sign_in () {
  //ajax获取日历json数据
  $.ajax ({
    type: "post",
    url: LIAO.BASE_URL + '/user/get_month_sign_in',
    data: '_token=' + LIAO._TOKEN,
    complete: function () {

    },
    error: function () {
      console.error ('签到本月获取失败');
      calUtil.init ([]);
    },
    success: function (data) {
      if (data.status == 1) {
        var signList = data.signList;
        calUtil.init (signList);
      } else {
        // console.error('签到获取失败'+data.info);
        calUtil.init ([]);
      }
    }
  });
}
//显示二维码
function qrcode_view(url){
  layer.open({
    type: 1,
    title: false,
    closeBtn: 0,
    area: ['320px','350px'],
    skin: 'layui-layer-nobg', //没有背景色
    shadeClose: true,
    content: '<img src="/tool/qrcode?url='+url+'" style="max-width: 100%;">'
  });
}
$ (function () {
  //API
  $ (".store-list .app-link-click").click (function () {
    var _url = $ (this).attr ('href');
    var _title = $ (this).parent ().find ('.title').text ();
    layer.open ({
      type: 2
      , title: _title
      , offset: '100px'
      , area: [ '80%', '68%' ]
      , shade: 0.6
      , shadeClose: false
      , scrollbar: false
      , id: 'layer-pop-app'
      , content: _url
    });
    return false;
  });

  //点击底部二维码
  $(".footer-right .qrcode img").click(function () {
    $(".footer-right .qrcode img").addClass('img-blurry');
    $(this).removeClass('img-blurry')
  })
});

/**
 * 评论
 */
$(function () {
  if($('#comment-post').length){
    var E = window.wangEditor;
    var editor = new E('#comment-post');
    editor.customConfig.menus = [
      'emoticon',  // 表情
      'link',  // 插入链接
      'code',  // 插入代码
      'undo',  // 撤销
      'redo'  // 重复
    ]
    //监控是否修改
    editor.customConfig.onchange = function (html) {
      // html 即变化之后的内容
      if( html == '<p><br></p>' || html == ''){
        //为空
        $("#commentForm input[name='pid']").val(0)
      }
    }
    editor.create();
    // console.log(editor);

  }
  $("#post-comment-btn").click(function () {
    var content = editor.txt.html();
    if (content == '<p><br></p>' || content == '') {
      layer.msg('请输入评论内容');
      return false;
    }
    var id = $(this).data('id');
    $("#commentForm input[name='content']").val(content)
    $("#post-comment-btn").prop('disabled','disabled')
    var params = $("#commentForm").serialize();
    $.ajax({
      type: 'post',
      url: '/comment/post-comment/'+id,
        data: params,
        success: function (data) {
      if (data.error === 0) {
        layer.msg(data.msg)
        editor.txt.clear()
      }else{
        layer.msg(data.msg)
      }
    },
    complete:function () {
      $("#post-comment-btn").removeProp('disabled')
    }
  })
    console.log(content);
  });
  $("#comment .reply-btn").click(function () {
    var uid = $(this).data('uid')
    editor.txt.html('<a class="at-author" href="/user/'+uid+'">@'+$(this).data('name')+'</a>&nbsp;')
    $("#commentForm input[name='pid']").val($(this).data('id'))
  })
})
function addCommentLike(id){
  $.post('/comment/post-add-like/'+id,function (data) {
    if(data.error === 0){
      $("#comment-"+id+">.comment-head>.commont-btn>.like-btn>.vote-count").text('('+ data.result.like_num +')')
      $("#comment-"+id+">.comment-head>.commont-btn>.like-btn").addClass('active')
    }else{
      layer.msg(data.msg)
    }
  })
}
//==========评论结束==========
