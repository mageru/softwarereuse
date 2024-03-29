package reuze;
//$HeadURL$
//----------------------------------------
//RTree implementation.
//Copyright (C) 2002-2004 Wolfgang Baer - WBaer@gmx.de
//
//This library is free software; you can redistribute it and/or
//modify it under the terms of the GNU Lesser General Public
//License as published by the Free Software Foundation; either
//version 2.1 of the License, or (at your option) any later version.
//
//This library is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//Lesser General Public License for more details.
//
//You should have received a copy of the GNU Lesser General Public
//License along with this library; if not, write to the Free Software
//Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//----------------------------------------


/**
 * <p>
 * Exception class for all exceptions regarding pagefiles
 * </p>
 *
 * @author Wolfgang Baer - WBaer@gmx.de
 */
public class le_ExceptionPageFile extends Exception {

    /**
     * Constructor for PageFileException.
     */
    public le_ExceptionPageFile() {
        super();
    }

    /**
     * Constructor for PageFileException.
     *
     * @param message
     */
    public le_ExceptionPageFile( String message ) {
        super( message );
    }
}
