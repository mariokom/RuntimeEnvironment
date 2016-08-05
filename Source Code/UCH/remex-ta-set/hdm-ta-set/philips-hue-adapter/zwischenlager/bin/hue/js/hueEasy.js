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
            socketPath = "http://openurc.org/TPL/coloured-light-bulb-1.0/ColouredLightBulbSocket",
            hueSockets = {},
            keys = [],
            session,
            sessions = {},
            insertEl = $('#' + hue.vars.insertID),
            chooseHueEl = $('#' + hue.vars.chooseHueEl),
            MAX_COLORS = hue.vars.maxColors,
            INIT_COLOR = hue.vars.initColor,
            updateInterval = hue.vars.updateInterval;
        
        // Store a reference to the hue object
        $.data(el, "hue", hue);
        
        //Private Functions
        methods = {
            init: function () {
                if (!chooseHueEl.length) {
                    alert('There is no Element with an Id ' + hue.vars.chooseHueWrapperID);
                }
                if (!insertEl.length) {
                    alert('There is no Element with an Id ' + hue.vars.insertID);
                }
                
                //Get all Sockets
                methods.socket.getAll();
                //Set update Intervall
                org_myurc_webclient_setUpdateInterval(updateInterval);

                if ($.isEmptyObject(hueSockets)) {
                    alert("No sockets found");
                } else {
                    //Add Links for all Lamps
                    methods.link.init();
                    //Add chooseable colors
                    methods.div.init();
                    //Init all Sessions to get Values
                    methods.session.initAll();
                }
                //Check if Browser supports the use of SVG
                methods.supportsSvg();
            },
            socket: {
                /*
                 * Write all Sockets in the Object hueSockets
                 * Use the TargetId as Key
                */
                getAll: function () {
                    var sockets = org_myurc_urchttp_getAvailableSockets();
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
                    var len = keys.length,
                        i = 0;

                    $.each(hueSockets, function (key, value) {
                        session = org_myurc_webclient_openSession(value.socketName, value.targetId, null);
                        methods.lamp.init(key);
                        
                        //Use for Beginn the first Session
                        if (i === len - 1 && value.targetId !== hueSockets[keys[0]].targetId) {
                            session = org_myurc_webclient_getSession(hueSockets[keys[0]].socketName, hueSockets[keys[0]].targetId);
                        }
                        i++;
                    });
                },
                set: function (key) {
                    if (session.targetId === hueSockets[key].targetId) {
                        console.log("Same Session");
                    } else {
                        session = org_myurc_webclient_getSession(hueSockets[key].socketName, hueSockets[key].targetId);
                        //Init Lamp after change of Session
                        methods.lamp.init(key);
                    }
                }
            },
            lamp: {
                init: function (key) {
                    var number = parseInt($('#' + key).attr('data-color'), 10);
                    if ($('#' + key).hasClass('active')) {
                        methods.div.setColor(null, number); 
                    }
                },
                getBrightness: function () {
                    return parseInt(org_myurc_webclient_getValue(socketPath + "#/Brightness", session.targetId, session), 10);
                },
                getSaturation: function () {
                    return parseInt(org_myurc_webclient_getValue(socketPath + "#/Saturation", session.targetId, session), 10);
                },
                getHueDegree: function () {
                    return parseInt(org_myurc_webclient_getValue(socketPath + "#/HueDegree", session.targetId, session), 10);
                },
                getHueLightState: function () {
                    return org_myurc_webclient_getValue(socketPath + "#/LightSwitch", session.targetId, session);
                },
                setBrightness: function (value) {
                    org_myurc_webclient_setValue(socketPath + "#/Brightness", value, session.targetId, session);
                },
                setSaturation: function (value) {
                    org_myurc_webclient_setValue(socketPath + "#/Saturation", value, session.targetId, session);
                },
                setHueDegree: function (value) {
                    org_myurc_webclient_setValue(socketPath + "#/HueDegree", value, session.targetId, session);
                },
                setHueLightState: function (value) {
                    org_myurc_webclient_setValue(socketPath + "#/LightSwitch", value, session.targetId, session);
                }
            },
            div: {
                init: function () {
                    var i,
                        hue = -180,
                        s = 80,
                        v = 100,
                        row = $('<div class="row"></div>');
                    for (i = 1; i <= MAX_COLORS; i++) {
                        var item = ($('<div/>').addClass('col')),
                            itemColor = $('<div/>').addClass('color'),
                            rgb = methods.color.hsvToRGB(methods.color.mapHueDegree(hue), s, v);

                        itemColor.css('background-color', 'rgb(' + rgb[0] + ',' + rgb[1] + ',' + rgb[2] + ')');
                        itemColor.click(methods.div.setColorOnClick);
                        itemColor.attr('data-hue', hue).attr('data-sat', s).attr('data-bri', v).attr('data-number', i);
                        item.outerWidth(100 / MAX_COLORS + '%');
                        item.append(itemColor);
                        row.append(item);
                        hue = hue + (360 / MAX_COLORS);
                    }
                    insertEl.append(row);
                },
                getActive: function () {
                    var activeColor;
                    insertEl.find('.col').each(function () {
                        if ($(this).children(".color").hasClass('active')) {
                            activeColor = $(this).children(".color");
                        }
                    });
                    return activeColor;
                },
                setColorOnClick: function () {
                    var hue = $(this).attr('data-hue'),
                        sat = $(this).attr('data-sat'),
                        bright = $(this).attr('data-bri'),
                        number = $(this).attr('data-number'),
                        activeLink = methods.link.getActive();
                    insertEl.find('.col').each(function () {
                        if ($(this).children(".color").hasClass('active')) {
                            $(this).children(".color").removeClass('active');
                        }
                    });
                    $(this).addClass('active');
                    methods.lamp.setHueDegree(hue);
                    methods.lamp.setSaturation(sat);
                    methods.lamp.setBrightness(bright);
                    activeLink.attr('data-color', number);
                },
                setColor: function (lastColor, number) {
                    var activeLink = methods.link.getActive();
                    if (lastColor != null) {
                        lastColor.removeClass('active');
                    } else {
                        insertEl.find('.col').each(function () {
                            if ($(this).children(".color").hasClass('active')) {
                                $(this).children(".color").removeClass('active');
                            }
                        });
                    }
                    console.log(number);
                    insertEl.find('.col').each(function () {
                        if (parseInt($(this).children(".color").attr('data-number'), 10) === number) {
                            var $this = $(this).children(".color");
                            var hue = $this.attr('data-hue'),
                                sat = $this.attr('data-sat'),
                                bright = $this.attr('data-bri');
                            
                            $this.addClass('active');
                            methods.lamp.setHueDegree(hue);
                            methods.lamp.setSaturation(sat);
                            methods.lamp.setBrightness(bright);
                            activeLink.attr('data-color', number);
                        }
                    });   
                }
            },   
            link: {
                init: function () {
                    var i = 1,
                        linkWidth,
                        linkWrapper = $('<div/>').addClass('links');
                    $.each(hueSockets, function (key, value) {
                        var link = $('<a/>');
                        link = link.attr('id', key).attr('data-number', i).attr('data-color', INIT_COLOR);
                        link.append('<span>' + key + '</span>');
                        if (i === 1) {
                            link.addClass('active');
                        }
                        linkWrapper.append(link);
                        link.click(methods.link.click);
                        i = i + 1;
                    });
                    chooseHueEl.append(linkWrapper);
                },
                click: function () {
                    var id = $(this).attr('id');
                    chooseHueEl.find('a').each(function () {
                        if ($(this).hasClass('active')) {
                            $(this).removeClass('active');
                        }
                    });
                    $(this).addClass('active');
                    methods.session.set(id);
                },
                setActiveHue: function (lastHue, newHueNumber) {
                    lastHue.removeClass('active');
                    chooseHueEl.children('.links').find('a').each(function () {
                        if (parseInt($(this).attr('data-number'), 10) === newHueNumber) {
                            $(this).addClass('active');
                            methods.session.set($(this).attr('id'));
                        }
                    });
                },
                getActive: function () {
                    var activeLink;
                    chooseHueEl.children('.links').find('a').each(function () {
                        if ($(this).hasClass('active')) {
                            activeLink = $(this);
                        }
                    });
                    return activeLink;
                },
                getTargetId: function () {
                    var link = methods.link.getActive();
                    if (link.length) {
                        return link.attr('id');
                    } else {
                        console.log("Can't get TargetId of active Link");
                    }
                },
                setBGColor: function (key) {
                    chooseHueEl.find('a').each(function () {
                        if ($(this).attr('id') === key) {
                            var rgb = methods.color.hsvToRGB(methods.color.mapHueDegree(hueDegree), saturation, brightness);
                            $(this).css('background-color', 'rgb(' + rgb[0] + ',' + rgb[1] + ',' + rgb[2] + ')');
                        }
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
            supportsSvg: function () {
                if (!document.implementation.hasFeature("http://www.w3.org/TR/SVG11/feature#Image", "1.1")) {
                    $('body').addClass('no-svg');
                }
            },
            finalize: function () {
                org_myurc_webclient_finalize();
            }
            
        };
        
    

    
        //Public Methods
        hue.changeColor = function (val) {
            var oldColor = methods.div.getActive(),
                oldNumber = parseInt(oldColor.attr('data-number'), 10);
            if (val === 1) {
                var newNumber = val + oldNumber;
                if (newNumber <= MAX_COLORS) {
                    methods.div.setColor(oldColor, newNumber);
                } else {
                    methods.div.setColor(oldColor, 1); 
                }
            } else {
                var newNumber = oldNumber - Math.abs(val);
                if ((newNumber < 1) === false) {
                    methods.div.setColor(oldColor, newNumber);
                } else {
                    methods.div.setColor(oldColor, MAX_COLORS); 
                }
            }
        };
        hue.changeHue = function (val) {
            var lastHue = methods.link.getActive();
            var oldNumber = parseInt(lastHue.attr('data-number'), 10);
            var linkLength = parseInt($('.links').children().length, 10);
            if (val === 1) {
                var newNumber = val + oldNumber;
                if (newNumber <= linkLength) {
                    methods.link.setActiveHue(lastHue, newNumber);
                } else {
                    methods.link.setActiveHue(lastHue, 1);
                }
            } else {
                var newNumber = oldNumber - Math.abs(val);
                if ((newNumber < 1) === false) {
                    methods.link.setActiveHue(lastHue, newNumber);
                } else {
                    methods.link.setActiveHue(lastHue, linkLength);
                }
            }
            
        };
    
        
        methods.init();
    
        $(window).on("unload", methods.finalize);
    };
    
    $.hueUCH.defaults = {
        insertID: 'insert',
        chooseHueEl: 'chooseHue',
        maxColors: 10,
        initColor: 4,
        updateInterval: 2000
    };
    
    $.fn.hueUCH = function (options, val) {
        if (options === undefined) { options = {}; }

        if (typeof options === "object" && val === undefined) {
            return this.each(function () {
                new $.hueUCH(this, options);
            });
        } else {
            // Helper strings to quickly perform functions on the Lamp
            var $hueUCH = $(this).data("hue");
            switch (options) {
            case "changeColor":
                $hueUCH.changeColor(val);
                break;
            case "changeHue":
                $hueUCH.changeHue(val);
                break;
            default:
                $.error('Method ' +  options + ' does not exist on hueUCH');
            }
        }
    };
}(jQuery));