package ar.utn.edu.frba.ddsi.services.storage;

import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {
  String uploadFile(MultipartFile file, String folderPath);

  void deleteFile(String uuid);

  void deleteFiles(Iterable<String> uuids);
}
