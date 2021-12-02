package com.fundraisey.backend.controller.fileupload;

//@Service
//public class FileStorageService {
//    private final Path fileStorageLocation;
//
//    Date date = new Date();
//    SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyhhmmss");
//    String strDate = formatter.format(date);
//
//    @Autowired
//    InvestorRepository investorRepository;
//
//    @Autowired
//    public FileStorageService(FileStorageProperties fileStorageProperties) {
//        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
//
//        try {
//            Files.createDirectories(this.fileStorageLocation);
//        } catch (Exception e) {
//            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", e);
//        }
//    }
//
//    public String storeFile(MultipartFile file) {
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        fileName = strDate + fileName;
//
//        try {
//            if (fileName.contains("..")) {
//                throw new FileStorageException("Sorry! Filename contains d path sequence " + fileName);
//            }
//
//            Path targetLocation = this.fileStorageLocation.resolve(fileName);
//            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//
//            return fileName;
//        } catch (IOException e) {
//            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
//        }
//    }
//
//    public Resource loadFileAsResource(String fileName) {
//        try {
//            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
//
//            Resource resource = new UrlResource(filePath.toUri());
//
//            if (resource.exists()) {
//                return resource;
//            } else {
//                throw new FileStorageException("File not found " + fileName);
//            }
//        } catch (MalformedURLException e) {
//            throw new FileStorageException("File not found: " + fileName, e);
//        }
//    }
//}
