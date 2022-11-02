package com.ironsoftware.ironpdf.staticapi;

import com.ironsoftware.ironpdf.internal.proto.IronPdfServiceGrpc;

class RpcClient {

    final IronPdfServiceGrpc.IronPdfServiceBlockingStub BlockingStub;

    final IronPdfServiceGrpc.IronPdfServiceFutureStub FutureStub;

    final IronPdfServiceGrpc.IronPdfServiceStub Stub;

    RpcClient(IronPdfServiceGrpc.IronPdfServiceBlockingStub blockingStub,
              IronPdfServiceGrpc.IronPdfServiceFutureStub futureStub,
              IronPdfServiceGrpc.IronPdfServiceStub stub) {
        BlockingStub = blockingStub;
        FutureStub = futureStub;
        Stub = stub;
    }
}