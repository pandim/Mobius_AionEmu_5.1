/*
 * This file is part of the Aion-Emu project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.templates.recipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LunaComponent")
public class LunaComponent
{
	@XmlElement(name = "luna_component")
	protected ArrayList<LunaComponentElement> luna_component;
	
	public Collection<LunaComponentElement> getComponents()
	{
		return luna_component != null ? luna_component : Collections.<LunaComponentElement> emptyList();
	}
}