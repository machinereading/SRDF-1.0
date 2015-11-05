/*
 * jQuery idleTimer plugin
 * version 0.9.100511
 * by Paul Irish.
 *   http://github.com/paulirish/yui-misc/tree/
 * MIT license
 * adapted from YUI idle timer by nzakas:
 *   http://github.com/nzakas/yui-misc/
 * updated to fix Chrome setTimeout issue by Zaid Zawaideh
 */
(function(b){b.idleTimer=function(f,c,e){var e=b.extend({startImmediately:!0,idle:!1,enabled:!0,timeout:3E4,events:"mousemove keydown DOMMouseScroll mousewheel mousedown touchstart touchmove"},e),c=c||document,g=function(a){"number"===typeof a&&(a=void 0);a=b.data(a||c,"idleTimerObj");a.idle=!a.idle;var d=+new Date-a.olddate;a.olddate=+new Date;a.idle&&d<e.timeout?(a.idle=!1,clearTimeout(b.idleTimer.tId),e.enabled&&(b.idleTimer.tId=setTimeout(g,e.timeout))):(a=jQuery.Event(b.data(c,"idleTimer",a.idle? "idle":"active")+".idleTimer"),b(c).trigger(a))},d=b.data(c,"idleTimerObj")||{};d.olddate=d.olddate||+new Date;if("number"===typeof f)e.timeout=f;else{if("destroy"===f)return f=c,d=b.data(f,"idleTimerObj")||{},d.enabled=!1,clearTimeout(d.tId),b(f).off(".idleTimer"),this;if("getElapsedTime"===f)return+new Date-d.olddate}b(c).on(b.trim((e.events+" ").split(" ").join(".idleTimer ")),function(){var a=b.data(this,"idleTimerObj");clearTimeout(a.tId);a.enabled&&(a.idle&&g(this),a.tId=setTimeout(g,a.timeout))}); d.idle=e.idle;d.enabled=e.enabled;d.timeout=e.timeout;e.startImmediately&&(d.tId=setTimeout(g,d.timeout));b.data(c,"idleTimer","active");b.data(c,"idleTimerObj",d)};b.fn.idleTimer=function(f,c){c||(c={});this[0]&&b.idleTimer(f,this[0],c);return this}})(jQuery);