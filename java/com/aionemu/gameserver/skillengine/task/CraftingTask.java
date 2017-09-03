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
package com.aionemu.gameserver.skillengine.task;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.CraftConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.StaticObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.templates.item.ItemCategory;
import com.aionemu.gameserver.model.templates.item.ItemQuality;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.recipe.RecipeTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CRAFT_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CRAFT_UPDATE;
import com.aionemu.gameserver.services.craft.CraftService;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class CraftingTask extends AbstractCraftTask
{
	protected RecipeTemplate recipeTemplate;
	protected ItemTemplate itemTemplate;
	protected ItemTemplate itemTemplateReal;
	protected int critCount;
	protected boolean crit = false;
	protected boolean purpleCrit = false;
	protected int maxCritCount;
	private final int bonus;
	
	public CraftingTask(Player requestor, StaticObject responder, RecipeTemplate recipeTemplate, int skillLvlDiff, int bonus)
	{
		super(requestor, responder, skillLvlDiff);
		this.recipeTemplate = recipeTemplate;
		maxCritCount = recipeTemplate.getComboProductSize();
		this.bonus = bonus;
	}
	
	private void craftSetup()
	{
		itemQuality = itemTemplateReal.getItemQuality();
		currentSuccessValue = 0;
		currentFailureValue = 0;
		maxSuccessValue = (int) Math.round((itemQuality.getQualityId() + 3) * 3.5) * 5;
		maxFailureValue = (int) Math.round((itemQuality.getQualityId() + 3) * 5.25) * 5;
	}
	
	@Override
	protected void analyzeInteraction()
	{
		final int critVal = Rnd.get(55000) / (skillLvlDiff + 1);
		if (critVal < CraftConfig.CRAFT_CHANCE_BLUE_CRIT)
		{
			critType = CraftCritType.BLUE;
		}
		else if ((critVal < CraftConfig.CRAFT_CHANCE_INSTANT) && (itemQuality.getQualityId() < ItemQuality.EPIC.getQualityId()))
		{
			critType = CraftCritType.INSTANT;
			currentSuccessValue = maxSuccessValue;
			return;
		}
		if (CraftConfig.CRAFT_CHECK_TASK)
		{
			if (task == null)
			{
				return;
			}
		}
		double mod = (Math.sqrt((double) skillLvlDiff / 450f) * 100f) + (Rnd.nextGaussian() * 10f);
		mod -= (double) itemQuality.getQualityId() / 2;
		if (mod < 0)
		{
			currentFailureValue -= (int) mod;
		}
		else
		{
			currentSuccessValue += (int) mod;
		}
		if (currentSuccessValue >= maxSuccessValue)
		{
			currentSuccessValue = maxSuccessValue;
		}
		else if (currentFailureValue >= maxFailureValue)
		{
			currentFailureValue = maxFailureValue;
		}
	}
	
	@Override
	protected void onFailureFinish()
	{
		PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(recipeTemplate.getSkillid(), itemTemplate, currentSuccessValue, currentFailureValue, 6));
		PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(), responder.getObjectId(), 0, 3), true);
	}
	
	@Override
	protected boolean onSuccessFinish()
	{
		if (checkCrit() && (recipeTemplate.getComboProduct(critCount) != null))
		{
			if (purpleCrit)
			{
				critCount++;
			}
			craftSetup();
			PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(recipeTemplate.getSkillid(), itemTemplateReal, maxSuccessValue, maxFailureValue, 3));
			return false;
		}
		else
		{
			if ((critCount > 0) && checkCrit())
			{
				PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(), responder.getObjectId(), 0, 2), true);
				PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(recipeTemplate.getSkillid(), itemTemplateReal, currentSuccessValue, currentFailureValue, 5));
				CraftService.finishCrafting(requestor, recipeTemplate, critCount, bonus);
				return true;
			}
			PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(), responder.getObjectId(), 0, 2), true);
			PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(recipeTemplate.getSkillid(), itemTemplateReal, currentSuccessValue, currentFailureValue, 5));
			CraftService.finishCrafting(requestor, recipeTemplate, critCount, bonus);
			return true;
		}
	}
	
	@Override
	protected void sendInteractionUpdate()
	{
		PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(recipeTemplate.getSkillid(), itemTemplate, currentSuccessValue, currentFailureValue, critType.getPacketId()));
		if (critType == CraftCritType.PURPLE)
		{
			critType = CraftCritType.NONE;
		}
	}
	
	@Override
	protected void onInteractionAbort()
	{
		PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(recipeTemplate.getSkillid(), itemTemplate, 0, 0, 4));
		PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(), responder.getObjectId(), 0, 2), true);
		requestor.setCraftingTask(null);
	}
	
	@Override
	protected void onInteractionFinish()
	{
		requestor.setCraftingTask(null);
	}
	
	@Override
	protected void onInteractionStart()
	{
		itemTemplate = DataManager.ITEM_DATA.getItemTemplate(recipeTemplate.getProductid());
		itemTemplateReal = itemTemplate;
		craftSetup();
		if ((recipeTemplate.getMaxProductionCount() != null) && (itemTemplateReal.getCategory() == ItemCategory.QUEST))
		{
			requestor.getRecipeList().deleteRecipe(requestor, recipeTemplate.getId());
		}
		int chance = requestor.getRates().getCraftCritRate();
		if (maxCritCount > 0)
		{
			if ((critCount > 0) && (maxCritCount > 1))
			{
				chance = requestor.getRates().getComboCritRate();
				final House house = requestor.getActiveHouse();
				if (house != null)
				{
					switch (house.getHouseType())
					{
						case ESTATE:
						case MANSION:
						case HOUSE:
						case STUDIO:
						case PALACE:
							chance += 5;
							break;
						default:
							break;
					}
				}
			}
			if ((critCount < maxCritCount) && (Rnd.get(100) < chance))
			{
				critCount++;
				crit = true;
			}
			if (((critCount > 0) && (critCount <= maxCritCount) && (maxCritCount != 1)) && (Rnd.get(100) < chance))
			{
				purpleCrit = true;
			}
		}
		PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(recipeTemplate.getSkillid(), itemTemplate, maxSuccessValue, maxFailureValue, 0));
		onInteraction();
		PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(), responder.getObjectId(), recipeTemplate.getSkillid(), 0), true);
		PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(), responder.getObjectId(), recipeTemplate.getSkillid(), 1), true);
	}
	
	@Override
	protected boolean onInteraction()
	{
		if (currentSuccessValue == maxSuccessValue)
		{
			return onSuccessFinish();
		}
		if (currentFailureValue == maxFailureValue)
		{
			onFailureFinish();
			return true;
		}
		analyzeInteraction();
		sendInteractionUpdate();
		return false;
	}
	
	private boolean checkCrit()
	{
		if (crit)
		{
			crit = false;
			itemTemplateReal = DataManager.ITEM_DATA.getItemTemplate(recipeTemplate.getComboProduct(critCount));
			return true;
		}
		if (purpleCrit)
		{
			purpleCrit = false;
			itemTemplateReal = DataManager.ITEM_DATA.getItemTemplate(recipeTemplate.getComboProduct(critCount));
			return true;
		}
		return false;
	}
}