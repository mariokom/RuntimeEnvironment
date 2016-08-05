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
            chooseHueEl = $('#' + hue.vars.chooseHueWrapperID),
            saturationEl = $('#' + hue.vars.saturationID),
            brightnessEl = $('#' + hue.vars.brightnessID),
            hueDegreeEl  = $('#' + hue.vars.hueDegreeID),
            switchOnOffEl = $('#' + hue.vars.switchOnOffID),
            updateInterval = hue.vars.updateInterval,
            brightness,
            saturation,
            hueDegree,
            lightState;
        
        // Store a reference to the hue object
        $.data(el, "hue", hue);
        
        //Private Functions
        methods = {
            init: function () {
                if (!chooseHueEl.length) {
                    alert('There is no Element with an Id ' + hue.vars.chooseHueWrapperID);
                }
                if (!saturationEl.length) {
                    alert('There is no Element with an Id ' + hue.vars.saturationID);
                }
                if (!brightnessEl.length) {
                    alert('There is no Element with an Id ' + hue.vars.brightnessID);
                }
                if (!hueDegreeEl.length) {
                    alert('There is no Element with an Id ' + hue.vars.hueDegreeID);
                }
                if (!switchOnOffEl.length) {
                    alert('There is no Element with an Id ' + hue.vars.switchOnOffID);
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
                    //Init all Sessions to get Values
                    methods.session.initAll();
                }
            
                /*
                 * Check if Browser supports the use of SVG
                 * Currently useless because Chrome does not Support SVG in Pseudo-Element :after anyway.
                */
                methods.supportsSvg();
            },
            socket: {
                /*
                 * Write all Sockets in the Object hueSockets
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
                 * Init all Sessions to get all Values;
                */
                initAll: function () {
                    var len = keys.length,
                        i = 0;

                    $.each(hueSockets, function (key, value) {
                        session = org_myurc_webclient_openSession(value.socketName, value.targetId, null);
                        if (i === 0) {
                            methods.lamp.init(key);
                        } else {
                            methods.lamp.initNotFirst(key);
                        }                       
                        
                        // Using the fist session on beginning
                        if (i === len - 1 && value.targetId != hueSockets[keys[0]].targetId) {
                            session = org_myurc_webclient_getSession(hueSockets[keys[0]].socketName, hueSockets[keys[0]].targetId);
                            // methods.lamp.init(keys[0]);
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
                    brightness = methods.lamp.getBrightness();
                    saturation = methods.lamp.getSaturation();
                    hueDegree = methods.lamp.getHueDegree();
                    lightState = methods.lamp.getHueLightState();
                    
                    methods.output.setBrightness();
                    methods.output.setSaturation();
                    methods.output.setHueDegree();
                    
                    methods.input.setBrightness();
                    methods.input.setSaturation();
                    methods.input.setHueDegree();
                    methods.input.setSwitchOnOff();
                    
                    methods.link.setBGColor(key);
                    methods.output.background();

                },
                /*
                 * Just get the values of the light and set the Background-Color for the link
                */
                initNotFirst: function (key) {
                    brightness = methods.lamp.getBrightness();
                    saturation = methods.lamp.getSaturation();
                    hueDegree = methods.lamp.getHueDegree();
                    lightState = methods.lamp.getHueLightState();
                    
                    methods.link.setBGColor(key);
                    methods.lamp.isOn();
                    
                },
                isOn: function() {
                    if (lightState === true || lightState === 'true') {
                        if ($('#' + session.targetId).hasClass('off'))
                            $('#' + session.targetId).removeClass('off');
                    } else {
                        $('#' + session.targetId).addClass('off');
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
                }
            },
            link: {
                init: function () {
                    var i = 1,
                        linkWidth,
                        linkWrapper = $('<div/>').addClass('links');
                    $.each(hueSockets, function (key, value) {
                        var link = $('<a/>');
                        link = link.attr('id', key).attr('data-hue', i);
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
                        if (parseInt($(this).attr('data-hue'), 10) === newHueNumber) {
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
            
            output: {
                setBrightness: function () {
                    var output = brightnessEl.next('output'),
                        steps = (brightnessEl.outerWidth() - output.outerWidth()) / parseInt(brightnessEl.attr('max'), 10),
                        pos;
                    
                    if (brightness === 1 || brightness === '1') {
                        pos = 0;
                        console.log(pos);
                    } else {
                        pos = brightness * steps;
                    }
                    output.html(brightness);
                    output.css('left', pos);
                },
                setSaturation: function () {
                    var output = saturationEl.next('output'),
                        steps = (saturationEl.outerWidth() - output.outerWidth()) / parseInt(saturationEl.attr('max'), 10),
                        pos;
                    
                    if (saturation === 0) {
                        pos = 0;
                    } else {
                        pos = saturation * steps;
                    }
                    output.html(saturation);
                    output.css('left', pos);
                },
                setHueDegree: function () {
                    var output = hueDegreeEl.next('output'),
                        val = methods.color.mapHueDegree(hueDegree),
                        steps = (hueDegreeEl.outerWidth() - output.outerWidth()) / 360,
                        pos = val * steps
                    
                    output.html(hueDegree);
                    output.css('left', pos);
                },
                background: function () {
                    var h = methods.color.mapHueDegree(hueDegree),
                        hB,
                        dB,
                        hS,
                        dS,
                        h0,
                        h60,
                        h120,
                        h180,
                        h240,
                        h300,
                        h360,
                        style;

                    dB = methods.color.hsvToRGB(h, saturation, 100);
                    hS = methods.color.hsvToRGB(h, 0, brightness);
                    dS = methods.color.hsvToRGB(h, 100, brightness);
                    h0 = methods.color.hsvToRGB(0, saturation, brightness);
                    h60 = methods.color.hsvToRGB(60, saturation, brightness);
                    h120 = methods.color.hsvToRGB(120, saturation, brightness);
                    h180 = methods.color.hsvToRGB(180, saturation, brightness);
                    h240 = methods.color.hsvToRGB(240, saturation, brightness);
                    h300 = methods.color.hsvToRGB(300, saturation, brightness);
                    h360 = methods.color.hsvToRGB(360, saturation, brightness);
                            
                    style = $("<style type='text/css'></style>");
                        
                    if (!$('head').find('style').length) {
                        style.appendTo('head');
                    }
                    $('head').find('style').text('');
                                                
                    $('head').find('style').text("#brightness::-moz-range-track {" +
                        "background: linear-gradient(left, rgb(0,0,0) 0%, rgb(" + dB[0] + "," + dB[1] + "," + dB[2] + ") 100%);" +
                        "background: -moz-linear-gradient(left, rgb(0,0,0) 0%, rgb(" + dB[0] + "," + dB[1] + "," + dB[2] + ") 100%);}" +
                        "#brightness::-webkit-slider-runnable-track {" +
                        "background: linear-gradient(left, rgb(0,0,0) 0%, rgb(" + dB[0] + "," + dB[1] + "," + dB[2] + ") 100%);" +
                        "background: -webkit-linear-gradient(left, rgb(0,0,0) 0%, rgb(" + dB[0] + "," + dB[1] + "," + dB[2] + ") 100%);}" +
                        "#saturation::-moz-range-track {" +
                        "background: linear-gradient(left, rgb(" + hS[0] + "," + hS[1] + "," + hS[2] + ") 0%, rgb(" + dS[0] + "," + dS[1] + "," + dS[2] + ") 100%);" +
                        "background: -moz-linear-gradient(left, rgb(" + hS[0] + "," + hS[1] + "," + hS[2] + ") 0%, rgb(" + dS[0] + "," + dS[1] + "," + dS[2] + ") 100%);}" +
                        "#saturation::-webkit-slider-runnable-track {" +
                        "background: linear-gradient(left, rgb(" + hS[0] + "," + hS[1] + "," + hS[2] + ") 0%, rgb(" + dS[0] + "," + dS[1] + "," + dS[2] + ") 100%);" +
                        "background: -webkit-linear-gradient(left, rgb(" + hS[0] + "," + hS[1] + "," + hS[2] + ") 0%, rgb(" + dS[0] + "," + dS[1] + "," + dS[2] + ") 100%);}" +
                        "#hueDegree::-moz-range-track {" +
                        "background: linear-gradient(left, rgb(" + h0[0] + "," + h0[1] + "," + h0[2] + ") 0%, rgb(" + h60[0] + "," + h60[1] + "," + h60[2] + ") 16%, rgb(" + h120[0] + "," + h120[1] + "," + h120[2] + ") 32%, rgb(" + h180[0] + "," + h180[1] + "," + h180[2] + ") 48%, rgb(" + h240[0] + "," + h240[1] + "," + h240[2] + ") 64%, rgb(" + h300[0] + "," + h300[1] + "," + h300[2] + ") 80%, rgb(" + h360[0] + "," + h360[1] + "," + h360[2] + ") 100%);" +
                        "background: -moz-linear-gradient(left, rgb(" + h0[0] + "," + h0[1] + "," + h0[2] + ") 0%, rgb(" + h60[0] + "," + h60[1] + "," + h60[2] + ") 16%, rgb(" + h120[0] + "," + h120[1] + "," + h120[2] + ") 32%, rgb(" + h180[0] + "," + h180[1] + "," + h180[2] + ") 48%, rgb(" + h240[0] + "," + h240[1] + "," + h240[2] + ") 64%, rgb(" + h300[0] + "," + h300[1] + "," + h300[2] + ") 80%, rgb(" + h360[0] + "," + h360[1] + "," + h360[2] + ") 100%);}" +
                        "#hueDegree::-webkit-slider-runnable-track {" +
                        "background: linear-gradient(left, rgb(" + h0[0] + "," + h0[1] + "," + h0[2] + ") 0%, rgb(" + h60[0] + "," + h60[1] + "," + h60[2] + ") 16%, rgb(" + h120[0] + "," + h120[1] + "," + h120[2] + ") 32%, rgb(" + h180[0] + "," + h180[1] + "," + h180[2] + ") 48%, rgb(" + h240[0] + "," + h240[1] + "," + h240[2] + ") 64%, rgb(" + h300[0] + "," + h300[1] + "," + h300[2] + ") 80%, rgb(" + h360[0] + "," + h360[1] + "," + h360[2] + ") 100%);" +
                        "background: -webkit-linear-gradient(left, rgb(" + h0[0] + "," + h0[1] + "," + h0[2] + ") 0%, rgb(" + h60[0] + "," + h60[1] + "," + h60[2] + ") 16%, rgb(" + h120[0] + "," + h120[1] + "," + h120[2] + ") 32%, rgb(" + h180[0] + "," + h180[1] + "," + h180[2] + ") 48%, rgb(" + h240[0] + "," + h240[1] + "," + h240[2] + ") 64%, rgb(" + h300[0] + "," + h300[1] + "," + h300[2] + ") 80%, rgb(" + h360[0] + "," + h360[1] + "," + h360[2] + ") 100%);}"
                        );
                }
            },
            input: {
                setBrightness: function () {
                    brightnessEl.val(brightness);
                },
                setSaturation: function () {
                    saturationEl.val(saturation);
                },
                setHueDegree: function () {
                    hueDegreeEl.val(hueDegree);
                },
                setSwitchOnOff: function () {
                    if (lightState === true || lightState === 'true') {
                        switchOnOffEl.prop("checked", true);
                        if ($('#' + session.targetId).hasClass('off'))
                            $('#' + session.targetId).removeClass('off');
                    } else {
                        switchOnOffEl.prop("checked", false);
                        $('#' + session.targetId).addClass('off');
                    }
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
        hue.setBrightness = function (val) {
            brightness = val;
            org_myurc_webclient_setValue(socketPath + "#/Brightness", val, session.targetId, session);
            
            methods.input.setBrightness();
            methods.output.setBrightness();
            methods.link.setBGColor(session.targetId);
            methods.output.background();
        };
        hue.setSaturation = function (val) {
            saturation = val;
            org_myurc_webclient_setValue(socketPath + "#/Saturation", val, session.targetId, session);
            
            methods.input.setSaturation();
            methods.output.setSaturation();
            methods.link.setBGColor(session.targetId);
            methods.output.background();
        };
        hue.setHueDegree = function (val) {
            hueDegree = val;
            org_myurc_webclient_setValue(socketPath + "#/HueDegree", val, session.targetId, session);
            
            methods.input.setHueDegree();
            methods.output.setHueDegree();
            methods.link.setBGColor(session.targetId);
            methods.output.background();
        };
        hue.setLightState = function (val) {
            lightState = val;
            org_myurc_webclient_setValue(socketPath + "#/LightSwitch", val, session.targetId, session);
            methods.input.setSwitchOnOff();
        };
        hue.changeHue = function (val) {
            var lastHue = methods.link.getActive();
            var oldNumber = parseInt(lastHue.attr('data-hue'), 10);
            var linkLength = parseInt($('.links').children().length, 10);
            
            if (linkLength > 1) {
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
            }
        };
                
        
        methods.init();
        
        $(window).on("unload", methods.finalize);
        
        
        brightnessEl.on('input', function () {
            hue.setBrightness($(this).val());
        });
        saturationEl.on('input', function () {
            hue.setSaturation($(this).val());
        });
        hueDegreeEl.on('input', function () {
            hue.setHueDegree($(this).val());
        });
        
        switchOnOffEl.on('change', function () {
            if ($(this).is(':checked')) {
                if ($(this).val() === true || $(this).val() === 'true') {
                    hue.setLightState($(this).val());
                } else {
                    hue.setLightState(true);
                }
            } else {
                hue.setLightState(false);
            }
        });
    };
    
    $.hueUCH.defaults = {
        chooseHueWrapperID: 'chooseHue',
        saturationID:       'saturation',
        brightnessID:       'brightness',
        hueDegreeID:        'hueDegree',
        switchOnOffID:      'switch',
        updateInterval:     2000
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
            case "setSaturation":
                $hueUCH.setSaturation(val);
                break;
            case "setBrightness":
                $hueUCH.setBrightness(val);
                break;
            case "setHueDegree":
                $hueUCH.setHueDegree(val);
                break;
            case "setLightState":
                $hueUCH.setLightState(val);
                break;
            case "switchLight":
                $hueUCH.setLightState(val);
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