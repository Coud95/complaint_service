# ComplaintService

## Overview
ComplaintService is a RESTful API for managing complaints. It allows you to create, edit, retrieve, and list complaints.

## API Endpoints

### Get Complaint by ID
Retrieve information for a specific complaint.

- **URL:** `/complaint/{id}`
- **Method:** `GET`
- **Path Parameters:**
  - `id` (integer, required): ID of the complaint to return
- **Responses:**
  - `200 OK`: Successful operation
  - `404 Not Found`: Complaint not found
  - `default`: Unexpected error

### Get All Complaints
Retrieve a list of all complaints.

- **URL:** `/complaint`
- **Method:** `GET`
- **Responses:**
  - `200 OK`: Successful operation
  - `default`: Unexpected error

### Add a New Complaint
Create a new complaint.

- **URL:** `/complaint`
- **Method:** `POST`
- **Request Body:**
  - `Complaint` (object, required): The complaint to create
- **Responses:**
  - `201 Created`: Complaint created successfully
  - `400 Bad Request`: Invalid input
  - `default`: Unexpected error

### Edit an Existing Complaint
Edit an existing complaint.

- **URL:** `/complaint`
- **Method:** `PUT`
- **Request Body:**
  - `EditComplaint` (object, required): The complaint to edit
- **Responses:**
  - `200 OK`: Successful operation
  - `404 Not Found`: Complaint not found
  - `default`: Unexpected error

## Data Models

### Complaint
- **id** (integer): The ID of the complaint
- **productId** (integer, required): The ID of the product
- **description** (string, required): The description of the complaint
- **creationDate** (string): The creation date of the complaint
- **submitter** (Submitter, required): The submitter of the complaint
- **country** (string): The country of the complaint
- **submitCount** (integer): The number of times the complaint has been submitted

### Submitter
- **firstName** (string, required): The first name of the submitter
- **lastName** (string, required): The last name of the submitter
- **emailAddress** (string, required): The email address of the submitter

### EditComplaint
- **complaintId** (integer, required): The ID of the complaint to edit
- **description** (string, required): The new description of the complaint

### ComplaintArray
- **items** (array of Complaint): A list of complaints

### Error
- **code** (integer, required): The error code
- **message** (string, required): The error message

## Running the Application
To run the application, use the following command:

```bash
mvn spring-boot:run
