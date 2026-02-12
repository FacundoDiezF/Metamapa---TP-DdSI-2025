package ar.utn.edu.frba.ddsi.services.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3StorageService implements IStorageService {

  private final S3Client s3Client;

  @Value("${aws.s3.bucket-name}")
  private String bucketName;

  public S3StorageService(S3Client s3Client) {
    this.s3Client = s3Client;
  }

  @Override
  public String uploadFile(MultipartFile file, String folderPath) {
    String fileName = UUID.randomUUID().toString();

    String key = (folderPath != null && !folderPath.isEmpty())
        ? folderPath + "/" + fileName
        : fileName;

    try {
      PutObjectRequest putOb = PutObjectRequest.builder()
          .bucket(bucketName)
          .key(key)
          .contentType(file.getContentType())
          .build();

      s3Client.putObject(putOb, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

      return key;
    } catch (IOException e) {
      throw new RuntimeException("Error reading file", e);
    }
  }

  @Override
  public void deleteFile(String path) {
    try {
      DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
          .bucket(bucketName)
          .key(path)
          .build();

      s3Client.deleteObject(deleteRequest);

    } catch (Exception e) {
      throw new RuntimeException("Error borrando archivo de S3: " + path, e);
    }
  }

  @Override
  public void deleteFiles(Iterable<String> paths) {
    for (String uuid : paths) {
      try{
        deleteFile(uuid);
      } catch (Exception e) {
        // Log error and continue
        System.err.println("Error deleting file with UUID: " + uuid + ". " + e.getMessage()); // tiene que haber una mejor solución que println para logs
      }
    }
  }

  @Override
  public S3Resource getFile(String path) {
    try {
      // Preparamos el pedido
      GetObjectRequest request = GetObjectRequest.builder()
          .bucket(bucketName)
          .key(path) // ej: "events/uuid-123..."
          .build();

      // Obtenemos el Stream (No descarga todo, solo abre conexión)
      ResponseInputStream<GetObjectResponse> s3Stream = s3Client.getObject(request);
      GetObjectResponse response = s3Stream.response();

      return S3Resource.builder()
          .inputStream(s3Stream)
          .contentType(response.contentType())
          .contentLength(response.contentLength())
          .filename(path)
          .build();

    } catch (Exception e) {
      // Si no existe, tiramos error (puedes personalizar la excepción)
      throw new RuntimeException("Error al recuperar archivo de S3: " + path, e);
    }
  }
}