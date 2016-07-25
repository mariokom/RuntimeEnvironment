(function() {
'use strict';

/**
 * 
 */
class SituationFactory {
  /**
   * 
   */
  constructor(adaptationEngine) {
    /** The adaptation engine instance for managing adaptations of produced situations */
    this._engine = adaptationEngine;
  }
  
  /**
   * 
   */
  create(situation, parameters, context) {
    
  }
  
  /**
   * 
   */
  refresh(situation, parameters, context) {
    
  }
}

// Register in namespace
myui.SituationFactory = SituationFactory;

}());