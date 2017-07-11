package me.ddevil.mirai.storage.local

import me.ddevil.mirai.Mirai
import me.ddevil.mirai.storage.StorageAccessor
import me.ddevil.mirai.storage.StorageLoader
import me.ddevil.mirai.storage.StorageManager
import me.ddevil.mirai.storage.StorageSavable
import me.ddevil.util.exception.WTFException
import java.io.File

class FileStorageManager(val dataHandler: DataHandler) : StorageManager {


    override fun <T> getData(accessor: StorageLoader<T>, data: String): T? {
        val foundData = getData(accessor as StorageAccessor, data) ?: return null
        return accessor.load(foundData)
    }

    val folder = File("./storage")

    private val subFolders = HashMap<StorageAccessor, File>()

    override fun getData(accessor: StorageAccessor, data: String): Map<String, Any?>? {
        val subFolder = getSubFolder(accessor)
        val file = getFile(subFolder, data, dataHandler)
        return dataHandler.load(file)
    }


    private fun getSubFolder(accessor: StorageAccessor) = subFolders.getOrPut(accessor) {
        val folder = getFile(folder, accessor.storageKey)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        return@getOrPut folder
    }

    override fun saveData(accessor: StorageAccessor, data: StorageSavable) {
        val file = getFile(getSubFolder(accessor), data.storagePath)
        dataHandler.save(data, file)
    }


    private fun getFile(subFolder: File, data: String, onLast: (String, File) -> (File)): File {
        val split = data.split('.')
        val last = split.lastIndex
        var lastFile = subFolder
        for ((index, name) in split.withIndex()) {
            val file: File
            if (index == last) {
                file = onLast(name, lastFile)
                //Is file, also last
                return file
            } else {
                file = File(lastFile, name)
            }
            //Is folder
            if (!file.exists()) {
                file.mkdir()
            }
            lastFile = file
        }
        throw WTFException()
    }


    private fun getFile(subFolder: File, data: String) = getFile(subFolder, data) {
        name, parent ->
        File(parent, name)
    }

    private fun getFile(subFolder: File, data: String, dataHandler: DataHandler) = getFile(subFolder, data) {
        name, parent ->
        File(parent, "$name.${dataHandler.fileExtension}")
    }

}
