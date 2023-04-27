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

window._import=e=>import(e);const appRoot=document.querySelector("base").getAttribute("app-root"),baseImportPath=`${appRoot}/app/tasklist/`,loadConfig=async function(){const e=(await import(baseImportPath+"scripts/config.js?bust="+(new Date).getTime())).default||{};return window.camTasklistConf=e,e}();window.__define("camunda-tasklist-bootstrap",["./scripts/camunda-tasklist-ui"],(function(){loadConfig.then((e=>{!function(e){"use strict";var i=window.CamundaTasklistUi;requirejs.config({baseUrl:"../../../lib"});var a=window;i.exposePackages(a),window.define=window.__define,window.require=window.__require,requirejs(["globalize"],(function(t){t(requirejs,["angular","camunda-commons-ui","camunda-bpm-sdk-js","jquery","angular-data-depend"],a);var n=window.PLUGIN_PACKAGES||[],s=window.PLUGIN_DEPENDENCIES||[];n=n.filter((e=>"tasklist-plugin-tasklistPlugins"===e.name||e.name.startsWith("tasklist-plugin-legacy"))),s=s.filter((e=>"tasklist-plugin-tasklistPlugins"===e.requirePackageName||e.requirePackageName.startsWith("tasklist-plugin-legacy"))),n.forEach((function(e){var i=document.createElement("link");i.setAttribute("rel","stylesheet"),i.setAttribute("href",e.location+"/plugin.css"),document.head.appendChild(i)})),requirejs.config({packages:n,baseUrl:"../",paths:{ngDefine:"../../lib/ngDefine"}});var o=["angular","ngDefine"].concat(s.map((function(e){return e.requirePackageName})));requirejs(o,(function(a){if(window.camTasklistConf&&window.camTasklistConf.csrfCookieName&&a.module("cam.commons").config(["$httpProvider",function(e){e.defaults.xsrfCookieName=window.camTasklistConf.csrfCookieName}]),void 0!==window.camTasklistConf&&window.camTasklistConf.requireJsConfig){var t=e.requireJsConfig||{},n={};["baseUrl","paths","bundles","shim","map","config","packages","waitSeconds","context","callback","enforceDefine","xhtml","urlArgs","scriptType"].forEach((function(e){t[e]&&(n[e]=t[e])})),requirejs.config(n),requirejs(t.deps||[],(function(){a.module("cam.tasklist.custom",t.ngDeps),window.define=void 0,window.require=void 0,i(s)}))}else a.module("cam.tasklist.custom",[]),require([],(function(){window.define=void 0,window.require=void 0,i(s)}))}))}))}(e)}))})),requirejs(["camunda-tasklist-bootstrap"],(function(){}));