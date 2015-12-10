/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package termproj;

/**
 * Title:        Project #7
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public interface Comparator {

    public boolean isLessThan (Object obj1, Object obj2);

    public boolean isLessThanOrEqualTo (Object obj1, Object obj2);

    public boolean isGreaterThan (Object obj1, Object obj2);

    public boolean isGreaterThanOrEqualTo (Object obj1, Object obj2);

    public boolean isEqual (Object obj1, Object obj2);

    public boolean isComparable (Object obj);
}
