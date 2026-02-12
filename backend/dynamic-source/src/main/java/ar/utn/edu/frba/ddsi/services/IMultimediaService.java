package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.services.storage.S3Resource;

public interface IMultimediaService {
  S3Resource getMultimediaByPath(String path);
}
