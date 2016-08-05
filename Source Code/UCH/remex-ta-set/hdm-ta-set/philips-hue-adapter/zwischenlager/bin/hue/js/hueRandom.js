/* 
 * @requires jQuery
 * @requires org_myurc_webclient.js
 * @requires org_myurc_socket.js
 * @requires org_myurc_urchttp.js
 * @requires org_myurc_lib.js
 */

(function ($) {
    'use strict';
    $.hueUCH = function (el, options) {
        
        var hue = $(el);
        hue.vars = $.extend({}, $.hueUCH.defaults, options);
        var methods = {},
            keys = [],
            sockets = [],
            hueSockets = {},
            socketPath = "http://openurc.org/TPL/coloured-light-bulb-1.0/ColouredLightBulbSocket",
            session,
            sessions = {},
            hueEl = $('#' + hue.vars.hueWrapperID),
            updateInterval = hue.vars.updateInterval,
            hueColor;
    
        // Store a reference to the hue object
        $.data(el, "hue", hue);
        
        
        //Private Functions
        methods = {
            init: function () {
                
                //Get all Sockets
                methods.socket.getAll();
                //Set update Intervall
                org_myurc_webclient_setUpdateInterval(updateInterval);

                if ($.isEmptyObject(hueSockets)) {
                    alert("No sockets found");
                } else {
                    //Add Links for all Lamps
                    methods.div.init();
                    //Init all Sessions to get Values
                    methods.session.initAll();
                    

                }
                
            },
            socket: {
                /*
                 * Write all Sockets in the Object hueSockets
                 * Use the TargetId as Key
                */
                getAll: function () {
                    sockets = org_myurc_urchttp_getAvailableSockets();
                    sockets.forEach(function (socket) {
                        if (socket.socketName === socketPath && socket.targetId !== 'bridge') {
                            hueSockets[socket.targetId] = socket;
                            keys.push(socket.targetId);
                        }
                    });
                }
            },
            session: {
                /*
                 * Init all Sessions to get all Values;8
                */
                initAll: function () {
                    $.each(hueSockets, function (key, value) {
                        session = org_myurc_webclient_openSession(value.socketName, value.targetId, null);
                        methods.lamp.init(value.targetId);
                    });
                },
                set: function (key) {
                    if (session.targetId === key) {
                        console.log("Same Session");
                    } else {
                        session = org_myurc_webclient_getSession(hueSockets[key].socketName, hueSockets[key].targetId);
                        //Init Lamp after Session
                        methods.lamp.init(hueSockets[key].targetId);
                    }
                }
            },
            lamp: {
                init: function (key) {
                    hueColor = methods.lamp.getColor();
                    
                    methods.div.setBGColor(key);
                },
                
                setColor: function (h, s, v) {
                    org_myurc_webclient_setValue(socketPath + "#/HueDegree", h, session.targetId, session);
                    org_myurc_webclient_setValue(socketPath + "#/Saturation", s, session.targetId, session);
                    org_myurc_webclient_setValue(socketPath + "#/Brightness", v, session.targetId, session);
                },
                getColor: function () {
                    var hsv = [];
                    hsv[0] = parseInt(org_myurc_webclient_getValue(socketPath + "#/HueDegree", session.targetId, session), 10);
                    hsv[1] = parseInt(org_myurc_webclient_getValue(socketPath + "#/Saturation", session.targetId, session), 10);
                    hsv[2] = parseInt(org_myurc_webclient_getValue(socketPath + "#/Brightness", session.targetId, session), 10);
                    return hsv;
                }
            },
            div: {
                init: function () {
                    var i = 0,
                        divWidth;
                    $.each(hueSockets, function (key, value) {
                    //hueSockets.forEach(function (socket) {
                        var link = $('<div/>');
                        link = link.attr('id', key);
                        link.addClass('hue');
                        link.append('<h2>' + key + '</h2>');
                        hueEl.append(link);
                        i = i + 1;
                    });
                    divWidth = 100 / i;
                    hueEl.find('.hue').each(function () {
                        $(this).width(divWidth + '%');
                    });
                },
                setBGColor: function (key) {
                    var rgb;
                    rgb = methods.color.hsvToRGB(hueColor[0], hueColor[1], hueColor[2]);
                    $('#' + key).css('background-color', 'rgb(' + rgb[0] + ',' + rgb[1] + ',' + rgb[2] + ')');
                },
                
                randomBGColor: function () {
                    $.each(hueSockets, function (key, value) {
                        var h,
                            s,
                            v,
                            rgb = [];
                        methods.session.set(key);
                        
                        h = Math.floor((Math.random() * 360) + 1 - 180);
                        s = Math.floor((Math.random() * 100) + 1);
                        v = Math.floor((Math.random() * 100) + 1);
                        
                        methods.lamp.setColor(h, s, v);

                        hueColor = [h, s, v];

                        rgb = methods.color.hsvToRGB(methods.color.mapHueDegree(h), s, v);
                        $('#' + key).css('background-color', 'rgb(' + rgb[0] + ',' + rgb[1] + ',' + rgb[2] + ')');
                    });
                }
            },
            color: {
                mapHueDegree: function (hueDegree) {
                    var hue,
                        val = parseInt(hueDegree, 10);
                    if (val < 0) {
                        hue = Math.abs(val);
                        hue = 180 - hue;
                        return hue;
                    } else if (val === 0) {
                        hue = 180;
                        return hue;
                            
                    } else if (val > 0) {
                        hue = val + 180;
                        return hue;
                    }
                },
                hsvToRGB: function (h, s, v) {
                    var r,
                        g,
                        b,
                        i,
                        f,
                        p,
                        q,
                        t;
                            
                    // Make sure our arguments stay in-range
                    h = Math.max(0, Math.min(360, h));
                    s = Math.max(0, Math.min(100, s));
                    v = Math.max(0, Math.min(100, v));
                        
                    // Saturation and value (Brightness) are calculated from a range of 0 to 1. 
                    s /= 100;
                    v /= 100;
 
                    if (s === 0) {
                        r = g = b = v;
                        return [Math.round(r * 255), Math.round(g * 255), Math.round(b * 255)];
                    }
                        
                    h /= 60; // sector 0 to 5
                    i = Math.floor(h);
                    f = h - i; // factorial part of h
                    p = v * (1 - s);
                    q = v * (1 - s * f);
                    t = v * (1 - s * (1 - f));
                    console.log(i);
                    switch (i) {
                    case 0:
                        r = v;
                        g = t;
                        b = p;
                        break;

                    case 1:
                        r = q;
                        g = v;
                        b = p;
                        break;

                    case 2:
                        r = p;
                        g = v;
                        b = t;
                        break;
                    case 3:
                        r = p;
                        g = q;
                        b = v;
                        break;
                    case 4:
                        r = t;
                        g = p;
                        b = v;
                        break;
                    case 5:
                        r = v;
                        g = p;
                        b = q;
                        break;
                    case 6:
                        r = v;
                        g = t;
                        b = p;
                        break;
                    default:
                        console.log("No case could be choosed");
                        break;
                    }
                    console.log([Math.round(r * 255), Math.round(g * 255), Math.round(b * 255)]);
                    return [Math.round(r * 255), Math.round(g * 255), Math.round(b * 255)];
                }
            },
            finalize: function () {
                org_myurc_webclient_finalize();
            }
            
        };
        
        //Public Methods
        hue.random = function () {
            methods.div.randomBGColor();
        };
        
        
        
        methods.init();
        
        $(window).on("unload", methods.finalize);
        $('#top a').on("click", function (e) {
            e.preventDefault();
            methods.div.randomBGColor();
        });
    };
    
    $.hueUCH.defaults = {
        hueWrapperID    :   'hue',
        updateInterval  :   2000
    };
    
    $.fn.hueUCH = function (options) {
        if (options === undefined) { options = {}; }

        if (typeof options === "object") {
            return this.each(function () {
                new $.hueUCH(this, options);
            });
        } else {
            // Helper strings to quickly perform functions on the Lamp
            var $hueUCH = $(this).data("hue");
            switch (options) {
            case "random":
                $hueUCH.random();
                break;
            default:
                $.error('Method ' +  options + ' does not exist on hueUCH');
            }
        }
        
    };
}(jQuery));