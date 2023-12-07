package com.ssuspot.sns.application.service.post

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.PutObjectRequest
import com.drew.imaging.ImageMetadataReader
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
    fun uploadSingleImage(image: MultipartFile, email: String): ImageDto {
        val user = userService.findValidUserByEmail(email)
        val savedImageLink = uploadSingleFile(image)
        val metadata = ImageMetadataReader.readMetadata(image.inputStream)
        val directory = metadata.getFirstDirectoryOfType(com.drew.metadata.exif.GpsDirectory::class.java)
        val latitude = directory?.getGeoLocation()?.latitude ?: 0.0
        val longitude = directory?.getGeoLocation()?.longitude?: 0.0


        val savedImage = imageRepository.save(
            Image(
                imageUrl = savedImageLink.imageUrls,
                latitude = latitude,
                longitude = longitude,
                user = user
            )
        )
        return ImageDto(
            savedImage.id!!,
            savedImage.user.id!!,
            savedImage.imageUrl
        )
    }
    fun uploadMultipleFiles(files: List<MultipartFile>, email: String): List<ImageDto> {
        val imageUrls = mutableListOf<String>()
        val user = userService.findValidUserByEmail(email)

        val metadata = ImageMetadataReader.readMetadata(files[0].inputStream)
        val directory = metadata.getFirstDirectoryOfType(com.drew.metadata.exif.GpsDirectory::class.java)
        val latitude = directory?.getGeoLocation()?.latitude ?: 0.0
        val longitude = directory?.getGeoLocation()?.longitude?: 0.0


        for (file in files) {
            val fileObj: File = convertMultiPartFileToFile(file)
            val fileName = System.currentTimeMillis().toString() + "_" + file.originalFilename
            s3Client.putObject(PutObjectRequest(bucketName, fileName, fileObj))
            imageUrls.add(s3Client.getUrl(bucketName, fileName).toString())
            fileObj.delete()
        }
        val savedImages = imageRepository.saveAll(
            imageUrls.map {
                Image(
                    imageUrl = it,
                    user = user,
                    latitude = latitude,
                    longitude = longitude
                )
            }
        )

        return savedImages.map {
            ImageDto(
                it.id!!,
                it.user.id!!,
                it.imageUrl
            )
        }
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