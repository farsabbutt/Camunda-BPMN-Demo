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

window._import=e=>import(e);const appRoot=document.querySelector("base").getAttribute("app-root"),baseImportPath=`${appRoot}/app/admin/`,loadConfig=async function(){const e=(await import(baseImportPath+"scripts/config.js?bust="+(new Date).getTime())).default||{};return window.camAdminConf=e,e}();window.__define("camunda-admin-bootstrap",["./scripts/camunda-admin-ui"],(function(){"use strict";loadConfig.then((e=>{!function(e){var i=window.CamundaAdminUi;requirejs.config({baseUrl:"../../../lib"});var n=window;i.exposePackages(n),window.define=window.__define,window.require=window.__require,requirejs(["globalize"],(function(a){a(requirejs,["angular","camunda-commons-ui","camunda-bpm-sdk-js","jquery"],n);var o=window.PLUGIN_PACKAGES||[],r=window.PLUGIN_DEPENDENCIES||[];o=o.filter((e=>"admin-plugin-adminPlugins"===e.name||"admin-plugin-adminEE"===e.name||e.name.startsWith("admin-plugin-legacy"))),r=r.filter((e=>"admin-plugin-adminPlugins"===e.requirePackageName||"admin-plugin-adminEE"===e.requirePackageName||e.requirePackageName.startsWith("admin-plugin-legacy"))),o.forEach((function(e){var i=document.createElement("link");i.setAttribute("rel","stylesheet"),i.setAttribute("href",e.location+"/plugin.css"),document.head.appendChild(i)})),requirejs.config({packages:o,baseUrl:"../",paths:{ngDefine:"../../lib/ngDefine"}});var t=["angular","ngDefine"].concat(r.map((function(e){return e.requirePackageName})));requirejs(t,(function(n){if(e&&e.csrfCookieName&&n.module("cam.commons").config(["$httpProvider",function(i){i.defaults.xsrfCookieName=e.csrfCookieName}]),void 0!==e&&e.requireJsConfig){var a=e.requireJsConfig||{},o={};["baseUrl","paths","bundles","shim","map","config","packages","waitSeconds","context","callback","enforceDefine","xhtml","urlArgs","scriptType"].forEach((function(e){a[e]&&(o[e]=a[e])})),requirejs.config(o),requirejs(a.deps||[],(function(){n.module("cam.admin.custom",a.ngDeps),window.define=void 0,window.require=void 0,i(r)}))}else n.module("cam.admin.custom",[]),require([],(function(){window.define=void 0,window.require=void 0,i(r)}))}))}))}(e)}))})),requirejs(["camunda-admin-bootstrap"],(function(){}));