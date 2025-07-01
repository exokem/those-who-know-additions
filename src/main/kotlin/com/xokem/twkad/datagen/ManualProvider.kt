package com.xokem.twkad.datagen

import com.xokem.twkad.CurrencyItem
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Path

const val outputPath = "A:\\Projects\\those-who-know-additions\\src\\main\\resources\\data"

const val fillTemplate = """
{
  "type": "create:filling",
  "ingredients": [
    {
      "item": "@INPUT"
    },
    {
      "amount": @AMOUNT,
      "fluid": "@FLUID",
      "nbt": {}
    }
  ],
  "results": [
    {
      "item": "@OUTPUT"
    }
  ]
}
"""

const val deployTemplate = """
{
  "type": "create:deploying",
  "ingredients": [
    {
      "item": "@INPUT"
    },
    {
      "item": "@HELD"
    }
  ],
  "keepHeldItem": true,
  "results": [
    {
      "item": "@OUTPUT"
    }
  ]
}
"""

private fun write(content: String, file: String, vararg path: String)
{
    Files.createDirectories(Path.of(outputPath, *path))
    val path = Path.of(outputPath, *path, file)

    if (Files.exists(path))
        Files.delete(path)


    Files.createFile(path)
    val file = BufferedWriter(FileWriter(path.toString()))

    file.write(content)
    file.flush()
    file.close()
}

internal fun generate()
{
    CurrencyItem.values().forEach {
        write(fillTemplate
            .replace("@INPUT", "twkad:${it.formId}")
            .replace("@AMOUNT", "${it.amount}")
            .replace("@FLUID", it.material)
            .replace("@OUTPUT", "numismatics:${it.id}"),
            "${it.id}_fill.json", "create/recipes/filling"
            )
        write(deployTemplate
            .replace("@INPUT", "minecraft:clay_ball")
            .replace("@HELD", "twkad:${it.blankId}")
            .replace("@OUTPUT", "twkad:${it.formId}"),
            "${it.id}_form_deploy.json", "create/recipes/deploying",
        )
    }
}