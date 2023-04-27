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

window._import=e=>import(e);const appRoot=document.querySelector("base").getAttribute("app-root"),baseImportPath=`${appRoot}/app/welcome/`,loadConfig=async function(){const e=(await import(baseImportPath+"scripts/config.js?bust="+(new Date).getTime())).default||{};return window.camWelcomeConf=e,e}();window.__define("camunda-welcome-bootstrap",["./scripts/camunda-welcome-ui"],(function(){"use strict";loadConfig.then((e=>(e=>{var o=window.CamundaWelcomeUi;requirejs.config({baseUrl:"../../../lib"});var i=window;o.exposePackages(i),window.define=window.__define,window.require=window.__require,requirejs(["globalize"],(function(n){n(requirejs,["angular","camunda-commons-ui","camunda-bpm-sdk-js","jquery","angular-data-depend","moment","events"],i);var a=window.PLUGIN_PACKAGES||[],r=window.PLUGIN_DEPENDENCIES||[];a.forEach((function(e){var o=document.createElement("link");o.setAttribute("rel","stylesheet"),o.setAttribute("href",e.location+"/plugin.css"),document.head.appendChild(o)})),requirejs.config({packages:a,baseUrl:"../",paths:{ngDefine:"../../lib/ngDefine"}});var t=["angular","ngDefine"].concat(r.map((function(e){return e.requirePackageName})));requirejs(t,(function(i){if(e&&e.csrfCookieName&&i.module("cam.commons").config(["$httpProvider",function(o){o.defaults.xsrfCookieName=e.csrfCookieName}]),void 0!==e&&e.requireJsConfig){var n=e.requireJsConfig||{},a={};["baseUrl","paths","bundles","shim","map","config","packages","waitSeconds","context","callback","enforceDefine","xhtml","urlArgs","scriptType"].forEach((function(e){n[e]&&(a[e]=n[e])})),requirejs.config(a),requirejs(n.deps||[],(function(){i.module("cam.welcome.custom",n.ngDeps),window.define=void 0,window.require=void 0,o(r)}))}else i.module("cam.welcome.custom",[]),require([],(function(){window.define=void 0,window.require=void 0,o(r)}))}))}))})(e)))})),requirejs(["camunda-welcome-bootstrap"],(function(){}));