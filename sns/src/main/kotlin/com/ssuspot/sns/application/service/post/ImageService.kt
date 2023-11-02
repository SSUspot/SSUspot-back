package com.ssuspot.sns.application.service.post

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.PutObjectRequest
import com.ssuspot.sns.application.service.user.UserService
import com.ssuspot.sns.infrastructure.repository.post.ImageRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream

@Service
class ImageService(
    private val s3Client: AmazonS3,
    private val userService: UserService,
    private val imageRepository: ImageRepository
) {
    /*
    fun uploadFile(files: List<MultipartFile>): StorageResponseDto{
        val imageUrls = mutableListOf<String>()

        for (file in files) {
            val fileObj: File = convertMultiPartFileToFile(file)
            val fileName = System.currentTimeMillis().toString() + "_" + file.originalFilename
            s3Client.putObject(PutObjectRequest(bucketName, fileName, fileObj))
            imageUrls.add(s3Client.getUrl(bucketName, fileName).toString())
            fileObj.delete()
        }
        return StorageResponseDto(imageUrls.joinToString(","))
    }

    private fun convertMultiPartFileToFile(file: MultipartFile): File {
        val convertedFile = File(file.originalFilename)
        try {
            FileOutputStream(convertedFile).use { fos -> fos.write(file.bytes) }
        } catch (e: Exception) {
            //오류 throw
        }
        return convertedFile
    }

     */
}