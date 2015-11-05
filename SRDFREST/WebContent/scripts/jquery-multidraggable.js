/**
 * JQuery MultiDraggable Plugin
 *
 * Licensed under the MIT (http://www.opensource.org/licenses/mit-license.php)
 *
 * Written by Sudheer Someshwara <sudheer.someshwara@gmail.com>
 *
 * MultiDraggable is a jQuery plugin which extends jQuery UI Draggable to add multi drag and live functionality.
 *
**/
(function(b){b.fn.multiDraggable=function(a){var e=[],f=[];return this.each(function(){b(this).live("mouseover",function(){b(this).data("init")||b(this).data("init",!0).draggable(a,{start:function(){var c=b(this).position();b.each(a.group||{},function(a,d){var g=b(d).position();e[a]=g.left-c.left;f[a]=g.top-c.top});a.startNative&&a.startNative()},drag:function(){var c=b(this).offset();b.each(a.group||{},function(a,d){b(d).offset({left:c.left+e[a],top:c.top+f[a]})});a.dragNative&&a.dragNative()},stop:function(){var c= b(this).offset();b.each(a.group||{},function(a,d){b(d).offset({left:c.left+e[a],top:c.top+f[a]})});a.stopNative&&a.stopNative()}})})})}})(jQuery);