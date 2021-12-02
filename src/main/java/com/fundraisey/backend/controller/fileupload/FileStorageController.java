package com.fundraisey.backend.controller.fileupload;

//@RestController
//public class FileStorageController {
//    private final static Logger logger = LoggerFactory.getLogger(FileStorageController.class);
//
//    @Value("${app.uploadto.cdn}")
//    private String UPLOAD_FOLDER;
//
//    @Autowired
//    private FileStorageService fileStorageService;
//
//    @Autowired
//    private InvestorRepository investorRepository;
//
//    public FileStorageController() {
//    }
//
//    @RequestMapping(value = "/v1/upload", method = RequestMethod.POST, consumes = {"multipart/form-data", "application/json"})
//    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file,
//                                         @RequestParam(value = "id", required = false) Long id) throws IOException {
//        String fileName;
//        logger.info("Content type: " + file.getContentType());
//        if (!((file.getContentType().equals("image/png")) || (file.getContentType().equals("image/jpeg")))) {
//            return new UploadFileResponse(file.getOriginalFilename(), null, file.getContentType(), file.getSize(), "File is not a .png or .jpeg file");
//        }
//        try {
//            fileName = fileStorageService.storeFile(file);
//        } catch (Exception e) {
//            return new UploadFileResponse(file.getOriginalFilename(), null, file.getContentType(), file.getSize(), e.getMessage());
//        }
//        if (id != null) {
//            Investor objInvestor = investorRepository.getById(id);
//            objInvestor.setProfilePicture(fileName);
//            investorRepository.save(objInvestor);
//        }
//
//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("v1/showfile/")
//                .path(fileName)
//                .toUriString();
//
//        return new UploadFileResponse(fileName, fileDownloadUri,
//                file.getContentType(), file.getSize(), "false");
//
//    }
//
//    @GetMapping("v1/showfile/{fileName:.+}")
//    public ResponseEntity<Resource> showFile(@PathVariable String fileName, HttpServletRequest request) {
//        Resource resource = fileStorageService.loadFileAsResource(fileName);
//
//        String contentType = null;
//
//        try {
//            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
//        } catch (IOException e) {
//            logger.info("Couldn't determine content type");
//        }
//
//        if (contentType == null) {
//            contentType = "application/octet-stream";
//        }
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                .body(resource);
//    }
//
//    @PostMapping("v1/uploadMultipleFiles")
//    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) throws IOException {
//        return Arrays.asList(files)
//                .stream()
//                .map(file -> {
//                    try {
//                        return uploadFile(file, null);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    return null;
//                })
//                .collect(Collectors.toList());
//    }
//
//    // Unused
//    private File multipartToFile(MultipartFile upload, String routeName) {
//        String base = "";
//
//
//        logger.info(String.format("Trying upload file: %s", upload.getOriginalFilename()));
//
//        File file = new File(base + upload.getOriginalFilename());
//
//        try {
//            logger.info(String.format("Saving uploaded file to: '%s'", file.getAbsolutePath()));
//            FileOutputStream fos = new FileOutputStream(file);
//            fos.write(upload.getBytes());
//            fos.close();
//        } catch (IOException e) {
//            logger.error(String.format("Error: POST|UPLOAD %s", routeName), e);
//        }
//
//        return file;
//    }
//
//    private File multipartToFile(MultipartFile upload) {
//        return multipartToFile(upload, UPLOAD_FOLDER);
//    }
//
//}
