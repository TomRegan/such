package su.ch;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class TryTest {

    @Test void failedCompletionShouldCallIfFailedCode() {

        Receiver receiver = new Receiver();
        Finisher mockFinisher = spy(new Finisher());

        Try.with(receiver)
                .doAction(Receiver::method1)
                .doAction(Receiver::throwsException)
                .ifFailed(mockFinisher::failed);

        verify(mockFinisher, never()).success(any());
        verify(mockFinisher).failed(receiver);
    }

    @Test void successfulCompletionShouldCallIfSuccessCode() {

        Receiver receiver = new Receiver();
        Finisher mockFinisher = spy(new Finisher());

        Try.with(receiver)
                .doAction(Receiver::method1)
                .doAction(Receiver::method2)
                .ifSuccessful(mockFinisher::success);

        verify(mockFinisher).success(receiver);
    }

    @Test void failedCompletionShouldNotCallIfSuccessCode() {

        Receiver receiver = new Receiver();
        Finisher mockFinisher = spy(new Finisher());

        Try.with(receiver)
                .doAction(Receiver::method1)
                .doAction(Receiver::throwsException)
                .ifSuccessful(mockFinisher::success);

        verify(mockFinisher, never()).success(receiver);
    }


    @Test void doActionShouldNotTriggerActions() {

        Receiver mockReceiver = spy(new Receiver());

        Try.with(mockReceiver)
                .doAction(Receiver::method1);

        verify(mockReceiver, never()).method1();
    }

    @Test void doActionShouldSequenceActions() {

        Receiver mockReceiver = spy(new Receiver());
        InOrder inOrder = Mockito.inOrder(mockReceiver);

        Try.with(mockReceiver)
                .doAction(Receiver::method1)
                .doAction(Receiver::method2)
                .isSuccess();

        inOrder.verify(mockReceiver).method1();
        inOrder.verify(mockReceiver).method2();

    }

    @Test void rollbackShouldSequenceExceptionHandling() {

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


        int recover(@SuppressWarnings("unused") Throwable ignored) {

            return -1;
        }


        Object throwsException() {

            throw new RuntimeException("Error");
        }
    }

    class Finisher {

        void success(@SuppressWarnings("unused") Receiver receiver) {

        }

        void failed(@SuppressWarnings("unused") Receiver receiver) {

        }
    }
}
