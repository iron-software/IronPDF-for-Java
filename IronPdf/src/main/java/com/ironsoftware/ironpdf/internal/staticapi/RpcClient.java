package com.ironsoftware.ironpdf.internal.staticapi;

import com.ironsoftware.ironpdf.internal.proto.IronPdfServiceGrpc;

class RpcClient {

    final IronPdfServiceGrpc.IronPdfServiceBlockingStub blockingStub;

    final IronPdfServiceGrpc.IronPdfServiceFutureStub futureStub;

    final IronPdfServiceGrpc.IronPdfServiceStub stub;

    RpcClient(IronPdfServiceGrpc.IronPdfServiceBlockingStub blockingStub,
              IronPdfServiceGrpc.IronPdfServiceFutureStub futureStub,
              IronPdfServiceGrpc.IronPdfServiceStub stub) {
        this.blockingStub = blockingStub;
        this.futureStub = futureStub;
        this.stub = stub;
    }
}