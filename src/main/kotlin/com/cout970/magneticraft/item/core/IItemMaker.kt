package com.cout970.magneticraft.item.core

import net.minecraft.item.Item

/**
 * Created by cout970 on 2017/06/11.
 */
interface IItemMaker {

    fun initItems(): List<Item>
}