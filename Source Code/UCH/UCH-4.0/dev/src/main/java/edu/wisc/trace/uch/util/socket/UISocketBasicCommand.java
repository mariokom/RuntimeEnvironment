/*

Copyright (C) 2006-2013  Board of Regents of the University of Wisconsin System (Univ. of Wisconsin-Madison, Trace R&D Center).

This piece of the software package, developed by the Trace Center - University of Wisconsin is released to the public domain with only the following restrictions:

1) That the following acknowledgement be included in the source code and documentation for the program or package that use this code:

"Parts of this program were based on software developed by the Trace Center, University of Wisconsin-Madison under funding from NIDRR / US Dept of Education."

2) That this program not be modified unless it is plainly marked as modified from the original distributed by Trace.

NOTE: This license agreement does not cover third-party components bundled with this software, which have their own license agreement with them. A list of included third-party components with references to their license files is provided with this distribution. 

This software was developed under funding from NIDRR / US Dept of Education under grant # H133E030012.

THIS SOFTWARE IS EXPERIMENTAL/DEMONSTRATION IN NATURE. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT OF THIRD PARTY RIGHTS. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR HOLDERS INCLUDED IN THIS NOTICE BE LIABLE FOR ANY CLAIM, OR ANY SPECIAL INDIRECT OR CONSEQUENTIAL DAMAGES, OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.

*/

package edu.wisc.trace.uch.util.socket;

import java.util.HashMap;
import java.util.Map;

/**
 * UISocketBasicCommand is a variant of UISocketCommand. 
 * It provides the functionality to set/get the State of type="basicCommand".
 *
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
*/

public class UISocketBasicCommand extends UISocketCommand{

	
	private String state;
	
	
	/**
	 * Sets a command State.
	 * 
	 * @param state a String value of state
	 */
	public boolean setState(String state) {
		
		if ( isDimensional() ) {
			
			return false;
			
		} else {
			this.state = state;
			return true;
		}
	}
	
	/**
	 * Set Command State of specified indices.
	 * 
	 * @param state a String value of state
	 * @param indices a String value of indices
	 * 
	 * @return whether state set successfully or not
	 */
	public boolean setState( String state, String indices) {
		
		if( indices == null ) {
			
			if ( isDimensional() ) {
				return false;
			} else {
				this.state = state;
				return true;
			}
				
		} else {
			
			if ( isDimensional() ) {
				
				Map<String, CommandProps> commandPropsMap = getCommandProps(indices);
				
				if ( commandPropsMap == null )
					return false;
				
				if ( commandPropsMap.size() == 1 ) {
					
					for ( CommandProps commandProps : commandPropsMap.values() )
						commandProps.setState(state);
					
					return true;
					
				} else {
					
					return false;
				}
				
			} else {
				
				return false;
			}
				
		}
	}
	
	/**
	 * Get a Command State.
	 * 
	 * @return a String
	 */
	public String getState() {
		
		if ( isDimensional() )
			return null;
		else
			return state;
	}
	
	/**
	 * Get the state of specified indices
	 * 
	 * @param indices a String value of indices
	 * 
	 * @return a String value of state
	 */
	public String getState( String indices ) {
		
		if ( indices == null ) {
			
			if ( isDimensional() ) 		
				return null;
			else 		
				return this.state;
			
		} else {
			
			if ( isDimensional() ) {
				
				Map<String, CommandProps> commandPropsMap = getCommandProps(indices);
				
				if ( commandPropsMap == null )
					return null;
				
				if ( commandPropsMap.size() == 1 ) {
					
					for( CommandProps commandProps : commandPropsMap.values() )
						return commandProps.getState();
					
					return null;
				} else { 
					return null;
				}
				
			} else {
				return null;
			}
		}
	}
	
	/**
	 * Get states of specified indices.
	 * 
	 * @param indices a String value of indices
	 * 
	 * @return an Object of Map&lt;String, String&gt; representing state of specified indices.
	 */
	public Map<String, String> getRangeOfState(String indices) {
		//System.out.println("UISocketBasicCommand  : getRangeOfState : indices : "+indices);
		if ( (indices == null) || !isDimensional() ) 
			return null;
		
		Map<String, CommandProps> commandPropsMap = getCommandProps(indices);
		//System.out.println("UISocketBasicCommand  : getRangeOfState : commandPropsMap : "+commandPropsMap.values());
		if ( commandPropsMap == null )
			return null;
		
		Map<String, String> returnMap = new HashMap<String, String>();
		
		for ( String index : commandPropsMap.keySet() ) {
			
			CommandProps commandProps = commandPropsMap.get(index);
			
			if ( commandProps == null )
				continue;
			
			returnMap.put(index, commandProps.getState());
		}
		
		return returnMap;
	}
	/**
	 * @see edu.wisc.trace.uch.util.socket.IUISocketElement#clone()
	 */
	
	public Object clone() {
		
		UISocketBasicCommand clonedBasicCommand = new UISocketBasicCommand();
		
		clonedBasicCommand.setElementId( new String( this.getElementId() ) );
		clonedBasicCommand.setDimensional( this.isDimensional() );
		clonedBasicCommand.setSocket( this.getSocket() );
		
		if ( this.getSession() != null ) 
			clonedBasicCommand.setSession( this.getSession() );

		if ( this.getDimType() != null )
			clonedBasicCommand.setDimType( this.getDimType() );
		
		if ( this.getType() != null ) 
			clonedBasicCommand.setType( this.getType() );
		
		clonedBasicCommand.setCommandParams( getClonedCommandParams() );
	
		if( state != null )
			clonedBasicCommand.setState( new String(state) );
	
		return clonedBasicCommand;	
	}
	
	/**
	 * Get a Cloned copy of command parameters.
	 * 
	 * @return a Map&lt;String, UISocketCommandParam&gt;
	 */
	private HashMap<String, UISocketCommandParam> getClonedCommandParams() {
		
		HashMap<String, UISocketCommandParam> clonedCmdParams = new HashMap<String, UISocketCommandParam>();
		
		HashMap<String, UISocketCommandParam> commandParams = getCommandParamMap();
		
		for( String paramName : commandParams.keySet() ) 
			clonedCmdParams.put( new String(paramName), (UISocketCommandParam)commandParams.get(paramName).clone() );
		
		return clonedCmdParams;
	}
}
