package Interfaces.OperationsInterface.Operations.src.enquiriesManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EnquiriesManagement implements Operations_EnquiriesManagement_Interface {

    private List<MarketingEnquiry> enquiries;

    public EnquiriesManagement() {
        this.enquiries = new ArrayList<>();
    }

    @Override
    public List<MarketingEnquiry> getAllEnquiries() {
        return new ArrayList<>(enquiries); // Returns all enquiries
    }

    @Override
    public List<MarketingEnquiry> getEnquiries(String status) {
        return enquiries.stream()
                .filter(enquiry -> enquiry.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList()); // Filters enquiries by status
    }

    @Override
    public MarketingEnquiry getEnquiryDetails(String enquiryId) {
        return enquiries.stream()
                .filter(enquiry -> enquiry.getEnquiryId().equals(enquiryId))
                .findFirst()
                .orElse(null); // Finds and returns a specific enquiry by ID
    }

    // Method to add new enquiries (optional)
    public void addEnquiry(MarketingEnquiry enquiry) {
        enquiries.add(enquiry);
    }
}