package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.services.IMultimediaService;
import ar.utn.edu.frba.ddsi.services.storage.IStorageService;
import ar.utn.edu.frba.ddsi.services.storage.S3Resource;
import org.springframework.stereotype.Service;

@Service
public class MultimediaService implements IMultimediaService {

  private final IStorageService storageService;

  public MultimediaService(IStorageService storageService) {
    this.storageService = storageService;
  }

  @Override
  public S3Resource getMultimediaByPath(String path) {
    return storageService.getFile(path);
  }
}