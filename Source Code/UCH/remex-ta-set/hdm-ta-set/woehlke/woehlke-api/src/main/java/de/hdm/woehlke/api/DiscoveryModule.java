/*
Copyright 2015 Hochschule der Medien (HDM) / Stuttgart Media University

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. 

*/


package de.hdm.woehlke.api;

import java.util.Map;
/**
 * 
 * @author Lukas Smirek
 *Must be implemented by any TDM if any other module like an ConfigurationTool shall be able to call the TDM
 */
public interface DiscoveryModule {
/*
 * intended to be called byj any other module like an ConfigurationManager that wants to register a new target or to start a new discovery process 
 * @Param targetMap can contain information about the target to register
 */
	public void registerNewTarget(Map<String, String> targetMap);
}
