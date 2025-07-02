package com.xokem.twkad

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries
import org.apache.logging.log4j.LogManager
import java.io.InputStream

object XokMod
{
    const val ID = "twkad"
    private val OUT = LogManager.getLogger()

    /**
     * Attempts to retrieve the [java.io.InputStream] for an asset.
     *
     * @param path The path of the asset, relative to resources/.
     */
    fun getResource(path: String): InputStream?
    {
        val stream = javaClass.classLoader?.getResourceAsStream(path)

        if (stream == null)
        {
            warn("Unable to load resource '$path'")
            return null
        }

        return stream
    }

    /**
     * Attempts to retrieve the [java.io.InputStream] for a texture asset.
     *
     * The texture asset is assumed to be a *.png file.
     *
     * @param path The path of the texture asset, relative to resources/assets/voltaic/textures/.
     */
    fun getTexture(path: String) =
        getResource("assets/$ID/textures/$path")

    fun getItem(identifier: String) =
        ForgeRegistries.ITEMS.getValue(resource(identifier))!!

    fun getAnyItem(scopedIdentifier: String) =
        ForgeRegistries.ITEMS.getValue(ResourceLocation(scopedIdentifier))

    fun getBlock(identifier: String) =
        ForgeRegistries.BLOCKS.getValue(resource(identifier))!!

    fun resource(resource: String) =
        ResourceLocation(ID, resource)

    fun info(format: String, vararg args: Any) =
        OUT.info(String.format(format, *args))

    fun warn(format: String, vararg args: Any) =
        OUT.warn(String.format(format, *args))

    fun translate(key: String, vararg args: Any) =
        Component.translatable("$ID.$key", *args)

    fun translateBlock(key: String) =
        Component.translatable("block.$ID.$key")

    fun translate(key: String, colorRGB: Int, vararg args: Any) =
        translate(key, args).withStyle(Style.EMPTY.withColor(colorRGB))

    fun translateStyled(key: String, formatting: ChatFormatting?, vararg args: Any): MutableComponent
    {
        val component = translate(key, *args)
        component.setStyle(component.style.withColor(formatting))
        return component
    }
}