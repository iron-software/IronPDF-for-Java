package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.internal.proto.IronPdfServiceGrpc;
import io.grpc.ClientInterceptor;
import io.grpc.ClientInterceptors;
import io.grpc.ManagedChannel;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;

class RpcClient {

    final IronPdfServiceGrpc.IronPdfServiceBlockingStub GetBlockingStub(String methodName){
        return IronPdfServiceGrpc.newBlockingStub(ClientInterceptors.intercept(managedChannel,GetMetadata(methodName)));
    }

    final IronPdfServiceGrpc.IronPdfServiceStub GetStub(String methodName){
        return IronPdfServiceGrpc.newStub(ClientInterceptors.intercept(managedChannel,GetMetadata(methodName)));
    }

    final IronPdfServiceGrpc.IronPdfServiceFutureStub GetFutureStub(String methodName){
        return IronPdfServiceGrpc.newFutureStub(ClientInterceptors.intercept(managedChannel,GetMetadata(methodName)));
    }

    final ManagedChannel managedChannel;

    static final Metadata.Key<String> AUTHORIZATION_KEY =
            Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);


    RpcClient(ManagedChannel channel){
        this.managedChannel = channel;
    }

    private static ClientInterceptor GetMetadata(String methodName){
        Metadata metadata = new Metadata();
        metadata.put(Metadata.Key.of("ProductName", Metadata.ASCII_STRING_MARSHALLER), "IronPdf");
        metadata.put(Metadata.Key.of("LicenseKey", Metadata.ASCII_STRING_MARSHALLER), Setting_Api.licenseKey);
        metadata.put(Metadata.Key.of("ApiMethod", Metadata.ASCII_STRING_MARSHALLER), "Java_"+methodName);
        metadata.put(Metadata.Key.of("BuildTime", Metadata.ASCII_STRING_MARSHALLER), BuildInfo.BUILD_TIMESTAMP);
        // Opt-in only: IronPdfEngineConnection.withAuthToken sets the token. Without a token, no header is sent.
        String authToken = Access.engineAuthToken();
        if (authToken != null) {
            metadata.put(AUTHORIZATION_KEY, "Bearer " + authToken);
        }
        return MetadataUtils.newAttachHeadersInterceptor(metadata);
    }
}