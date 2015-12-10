/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package termproj;

/**
 * Title:        Term Project 2-4 Trees
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */


public class ElementNotFoundException extends RuntimeException {

    public ElementNotFoundException() {
        super ("Problem with TwoFourTree");
    }
    public ElementNotFoundException(String errorMsg) {
        super (errorMsg);
    }
}