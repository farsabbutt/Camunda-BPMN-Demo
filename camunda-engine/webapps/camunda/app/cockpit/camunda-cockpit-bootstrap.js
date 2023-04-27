/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

window._import=e=>import(e);const appRoot=document.querySelector("base").getAttribute("app-root"),baseImportPath=`${appRoot}/app/cockpit/`;function withSuffix(e,n){return e.endsWith(n)?e:e+n}const loadConfig=async function(){const e=(await import(baseImportPath+"scripts/config.js?bust="+(new Date).getTime())).default;if(Array.isArray(e.bpmnJs&&e.bpmnJs.additionalModules)){const n=e.bpmnJs.additionalModules.map((e=>import(withSuffix(baseImportPath+e,".js")))),i=await Promise.all(n);e.bpmnJs.additionalModules=i.map((e=>e.default))}return window.camCockpitConf=e,e}();window.__define("camunda-cockpit-bootstrap",["./scripts/camunda-cockpit-ui"],(function(){loadConfig.then((e=>{!function(e){"use strict";var n=window.CamundaCockpitUi;requirejs.config({baseUrl:"../../../lib"});var i=window;n.exposePackages(i),window.define=window.__define,window.require=window.__require,requirejs(["globalize"],(function(o){o(requirejs,["angular","camunda-commons-ui","camunda-bpm-sdk-js","jquery","angular-data-depend","moment","events"],i);var t=window.PLUGIN_PACKAGES||[],a=window.PLUGIN_DEPENDENCIES||[];t=t.filter((e=>"cockpit-plugin-cockpitPlugins"===e.name||"cockpit-plugin-cockpitEE"===e.name||e.name.startsWith("cockpit-plugin-legacy"))),a=a.filter((e=>"cockpit-plugin-cockpitPlugins"===e.requirePackageName||"cockpit-plugin-cockpitEE"===e.requirePackageName||e.requirePackageName.startsWith("cockpit-plugin-legacy"))),t.forEach((function(e){var n=document.createElement("link");n.setAttribute("rel","stylesheet"),n.setAttribute("href",e.location+"/plugin.css"),document.head.appendChild(n)})),requirejs.config({packages:t,baseUrl:"../",paths:{ngDefine:"../../lib/ngDefine"}});var c=["jquery","angular","ngDefine","moment"].concat(a.map((function(e){return e.requirePackageName})));requirejs(c,(function(i,o){if(window.camCockpitConf&&window.camCockpitConf.csrfCookieName&&o.module("cam.commons").config(["$httpProvider",function(e){e.defaults.xsrfCookieName=window.camCockpitConf.csrfCookieName}]),void 0!==e&&(e.requireJsConfig||e.bpmnJs)){var t=e.requireJsConfig||{},c={};["baseUrl","paths","bundles","shim","map","config","packages","waitSeconds","context","callback","enforceDefine","xhtml","urlArgs","scriptType"].forEach((function(e){t[e]&&(c[e]=t[e])})),c.paths=c.paths||{},t.deps=t.deps||[],t.ngDeps=t.ngDeps||[];var r=(e.bpmnJs||{}).additionalModules;r&&o.forEach(r,(function(e,n){c.paths[n]=r[n],t.deps.push(n)}));var s=(e.bpmnJs||{}).moddleExtensions;if(s){var u={};o.forEach(s,(function(e,n){u[n]=i.getJSON("../"+e+".json",(function(e){return e}))})),window.bpmnJsModdleExtensions={};var p=Object.keys(u).map((function(e){return u[e]}));i.when(p).then((function(){o.forEach(u,(function(e,n){e.done((function(e){window.bpmnJsModdleExtensions[n]=e})).fail((function(e){404===e.status?console.error('bpmn-js moddle extension "'+n+'" could not be loaded.'):console.error('unhandled error with bpmn-js moddle extension "'+n+'"')}))})),d()}))}else d()}else o.module("cam.cockpit.custom",[]),require([],(function(){m(a)}));function d(){requirejs.config(c),requirejs(t.deps||[],(function(){o.module("cam.cockpit.custom",t.ngDeps),m(a)}))}function m(e){window.define=void 0,window.require=void 0,n(e)}}))}))}(e)}))})),requirejs(["camunda-cockpit-bootstrap"],(function(){}));