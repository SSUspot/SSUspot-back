package com.ssuspot.sns.application.service.post

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.PutObjectRequest
import com.ssuspot.sns.application.dto.post.ImageDto
import com.ssuspot.sns.application.dto.post.StorageResponseDto
import com.ssuspot.sns.application.service.user.UserService
import com.ssuspot.sns.domain.model.post.entity.Image
import com.ssuspot.sns.infrastructure.repository.post.ImageRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

@Service
class ImageService(
    private val s3Client: AmazonS3,
    private val userService: UserService,
    private val imageRepository: ImageRepository,
    @Value("\${cloud.aws.s3.bucket}")
    private val bucketName: String
) {
    //위도 경도 일단 0.0으로 저장
    fun uploadSingleImage(image: MultipartFile, email: String): ImageDto {
        val user = userService.findValidUserByEmail(email)
        val savedImageLink = uploadSingleFile(image)
        val savedImage = imageRepository.save(
            Image(
                imageUrl = savedImageLink.imageUrls,
                latitude = 0.0,
                longitude = 0.0,
                user = user
            )
        )
        return ImageDto(
            savedImage.id!!,
            savedImage.user.id!!,
            savedImage.imageUrl
        )
    }
    fun uploadMultipleFiles(files: List<MultipartFile>): StorageResponseDto {
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
    //추후 이미지 여러개 업로드로 수정 가능성 있음
    fun uploadSingleFile(file: MultipartFile): StorageResponseDto {
        val imageUrls = mutableListOf<String>()
        val fileObj: File = convertMultiPartFileToFile(file)
        val fileName = System.currentTimeMillis().toString() + "_" + file.originalFilename
        s3Client.putObject(PutObjectRequest(bucketName, fileName, fileObj))
        imageUrls.add(s3Client.getUrl(bucketName, fileName).toString())
        fileObj.delete()
        return StorageResponseDto(imageUrls.joinToString(","))
    }

    private fun convertMultiPartFileToFile(file: MultipartFile): File {
        val convertedFile = File(file.originalFilename) ?: throw FileNotFoundException()
        FileOutputStream(convertedFile).use { fos -> fos.write(file.bytes) }
        return convertedFile
    }
}