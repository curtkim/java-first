package example;

import io.grpc.stub.StreamObserver;

public class HelloWorldServiceImpl extends HelloWorldServiceGrpc.HelloWorldServiceImplBase {

  @Override
  public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
    System.out.println("Handling hello endpoint: " + request.toString());

    String text = request.getText() + " World";
    HelloResponse response = HelloResponse.newBuilder().setText(text).build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}