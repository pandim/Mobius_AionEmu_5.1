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
package system.handlers.admincommands;

import java.io.IOException;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

public class FixZ extends AdminCommand
{
	
	public FixZ()
	{
		super("fixz");
	}
	
	@Override
	public void execute(Player admin, String... params)
	{
		if (admin.getAccessLevel() < 1)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to use this command!");
			return;
		}
		
		if (admin.getTarget() != null)
		{
			if (admin.getTarget() instanceof Npc)
			{
				final Npc target = (Npc) admin.getTarget();
				final SpawnTemplate temp = target.getSpawn();
				final int respawnTime = 295;
				final boolean permanent = true;
				
				// delete spawn,npc
				target.getController().delete();
				
				// spawn npc
				final int templateId = temp.getNpcId();
				final float x = temp.getX();
				final float y = temp.getY();
				final float z = admin.getZ();
				final byte heading = temp.getHeading();
				final int worldId = temp.getWorldId();
				
				final SpawnTemplate spawn = SpawnEngine.addNewSpawn(worldId, templateId, x, y, z, heading, respawnTime);
				
				if (spawn == null)
				{
					PacketSendUtility.sendMessage(admin, "There is no template with id " + templateId);
					return;
				}
				
				final VisibleObject visibleObject = SpawnEngine.spawnObject(spawn, admin.getInstanceId());
				
				if (visibleObject == null)
				{
					PacketSendUtility.sendMessage(admin, "npc id " + templateId + " was not found!");
				}
				else if (permanent)
				{
					try
					{
						DataManager.SPAWNS_DATA2.saveSpawn(admin, visibleObject, false);
					}
					catch (final IOException e)
					{
						PacketSendUtility.sendMessage(admin, "Could not save spawn");
					}
				}
				
				final String objectName = visibleObject.getObjectTemplate().getName();
				PacketSendUtility.sendMessage(admin, objectName + "FixZ");
			}
			
		}
		else
		{
			PacketSendUtility.sendMessage(admin, "Only in target!");
		}
	}
	
	protected VisibleObject spawn(int npcId, int mapId, int instanceId, float x, float y, float z, byte heading, String walkerId, int walkerIdx, int respawnTime)
	{
		final SpawnTemplate template = SpawnEngine.addNewSpawn(mapId, npcId, x, y, z, heading, respawnTime);
		return SpawnEngine.spawnObject(template, instanceId);
	}
	
}