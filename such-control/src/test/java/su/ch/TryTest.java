package su.ch;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class TryTest {


    @Test
    public void failedCompletionShouldCallIfFailedCode() throws Exception {

        Receiver receiver = new Receiver();
        Finisher mockFinisher = spy(new Finisher());

        Try.with(receiver)
                .doAction(Receiver::method1)
                .doAction(Receiver::throwsException)
                .ifFailed(mockFinisher::failed);

        verify(mockFinisher, never()).success(any());
        verify(mockFinisher).failed(receiver);
    }

    @Test
    public void successfulCompletionShouldCallIfSuccessCode() throws Exception {


        Receiver receiver = new Receiver();
        Finisher mockFinisher = spy(new Finisher());

        Try.with(receiver)
                .doAction(Receiver::method1)
                .doAction(Receiver::method2)
                .ifSuccessful((it) -> mockFinisher.success(it));

        verify(mockFinisher).success(receiver);
    }

    @Test
    public void failedCompletionShouldNotCallIfSuccessCode() throws Exception {


        Receiver receiver = new Receiver();
        Finisher mockFinisher = spy(new Finisher());

        Try.with(receiver)
                .doAction(Receiver::method1)
                .doAction(Receiver::throwsException)
                .ifSuccessful(mockFinisher::success);

        verify(mockFinisher, never()).success(receiver);
    }


    @Test
    public void doActionShouldNotTriggerActions() throws Exception {

        Receiver mockReceiver = spy(new Receiver());

        Try.with(mockReceiver)
                .doAction(Receiver::method1);

        verify(mockReceiver, never()).method1();
    }

    @Test
    public void doActionShouldSequenceActions() throws Exception {

        Receiver mockReceiver = spy(new Receiver());
        InOrder inOrder = Mockito.inOrder(mockReceiver);

        Try.with(mockReceiver)
                .doAction(Receiver::method1)
                .doAction(Receiver::method2)
                .isSuccess();

        inOrder.verify(mockReceiver).method1();
        inOrder.verify(mockReceiver).method2();

    }


    @Test
    public void rollbackShouldSequenceExceptionHandling() throws Exception {

        Receiver mockReceiver = spy(new Receiver());
        InOrder inOrder = Mockito.inOrder(mockReceiver);

        Try.with(mockReceiver)
                .doAction(Receiver::method1)
                .doAction(Receiver::throwsException)
                .orRecover((t, v) -> v.recover(t))
                .doAction(Receiver::method2)
                .isSuccess();

        inOrder.verify(mockReceiver).method1();
        inOrder.verify(mockReceiver).throwsException();
        inOrder.verify(mockReceiver).recover(any(Throwable.class));
        verify(mockReceiver, never()).method2();
    }


    class Receiver {

        int method1() {

            return hashCode();
        }


        int method2() {

            return hashCode();
        }


        int recover(Throwable ignored) {

            return -1;
        }


        Object throwsException() {

            throw new RuntimeException("Error");
        }
    }

    class Finisher {

        void success(Receiver receiver) {

        }

        void failed(Receiver receiver) {

        }
    }
}
