package com.ssuspot.sns.application.service.post

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.PutObjectRequest
import com.drew.imaging.ImageMetadataReader
import com.ssuspot.sns.application.dto.post.ImageDto
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
        val (latitude, longitude) = extractGeoLocation(image)
        val savedImageLink = uploadFileToS3(image)

        val savedImage = imageRepository.save(
            Image(imageUrl = savedImageLink, latitude = latitude, longitude = longitude, user = user)
        )
        return ImageDto(savedImage.id!!, savedImage.user.id!!, savedImage.imageUrl)
    }

    fun uploadMultipleFiles(files: List<MultipartFile>, email: String): List<ImageDto> {
        val user = userService.findValidUserByEmail(email)
        val (latitude, longitude) = extractGeoLocation(files.first())

        val savedImages = files.map { uploadFileToS3(it) }.map { imageUrl ->
            Image(imageUrl = imageUrl, user = user, latitude = latitude, longitude = longitude)
        }.let { imageRepository.saveAll(it) }

        return savedImages.map { ImageDto(it.id!!, it.user.id!!, it.imageUrl) }
    }

    private fun uploadFileToS3(file: MultipartFile): String {
        val fileObj = convertMultiPartFileToFile(file)
        val fileName = System.currentTimeMillis().toString() + "_" + file.originalFilename
        s3Client.putObject(PutObjectRequest(bucketName, fileName, fileObj))
        val imageUrl = s3Client.getUrl(bucketName, fileName).toString()
        fileObj.delete()
        return imageUrl
    }

    private fun extractGeoLocation(file: MultipartFile): Pair<Double, Double> {
        val metadata = ImageMetadataReader.readMetadata(file.inputStream)
        val directory = metadata.getFirstDirectoryOfType(com.drew.metadata.exif.GpsDirectory::class.java)
        return Pair(directory?.getGeoLocation()?.latitude ?: 0.0, directory?.getGeoLocation()?.longitude ?: 0.0)
    }

    private fun convertMultiPartFileToFile(file: MultipartFile): File {
        val convertedFile = File(file.originalFilename ?: throw FileNotFoundException("Original filename not found"))
        FileOutputStream(convertedFile).use { fos -> fos.write(file.bytes) }
        return convertedFile
    }
}
