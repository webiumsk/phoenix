/*
 * Copyright 2022 ACINQ SAS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.acinq.phoenix.android.payments

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.acinq.lightning.db.IncomingPayment
import fr.acinq.lightning.db.OutgoingPayment
import fr.acinq.lightning.db.WalletPayment
import fr.acinq.phoenix.android.LocalBitcoinUnit
import fr.acinq.phoenix.android.R
import fr.acinq.phoenix.android.components.*
import fr.acinq.phoenix.android.utils.Converter.toAbsoluteDateString
import fr.acinq.phoenix.android.utils.Converter.toPrettyString
import fr.acinq.phoenix.android.utils.MSatDisplayPolicy
import fr.acinq.phoenix.android.utils.mutedTextColor
import fr.acinq.phoenix.android.utils.negativeColor
import fr.acinq.phoenix.android.utils.positiveColor
import fr.acinq.phoenix.data.WalletPaymentId
import fr.acinq.phoenix.data.WalletPaymentInfo
import fr.acinq.phoenix.utils.extensions.errorMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun PaymentDetailsSplashView(
    data: WalletPaymentInfo,
    onDetailsClick: (WalletPaymentId) -> Unit,
    onMetadataDescriptionUpdate: (WalletPaymentId, String?) -> Unit,
    fromEvent: Boolean,
) {
    val payment = data.payment
    var showEditDescriptionDialog by remember { mutableStateOf(false) }

    // status
    PaymentStatus(payment, fromEvent)
    Spacer(modifier = Modifier.height(64.dp))

    // details
    Column(
        modifier = Modifier
            .widthIn(500.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(MaterialTheme.colors.surface)
            .padding(top = 40.dp, bottom = 0.dp, start = 0.dp, end = 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AmountWithAltView(
            amount = payment.amount,
            amountTextStyle = MaterialTheme.typography.h2,
            unitTextStyle = MaterialTheme.typography.h4,
            separatorSpace = 4.dp,
            isOutgoing = payment is OutgoingPayment
        )
        Spacer(modifier = Modifier.height(40.dp))

        DetailsRow(
            label = stringResource(id = R.string.paymentdetails_desc_label),
            value = when (payment) {
                is OutgoingPayment -> when (val details = payment.details) {
                    is OutgoingPayment.Details.Normal -> data.metadata.userDescription ?: details.paymentRequest.description ?: details.paymentRequest.descriptionHash?.toHex()
                    is OutgoingPayment.Details.ChannelClosing -> "Closing channel ${details.channelId}"
                    is OutgoingPayment.Details.KeySend -> "Donation"
                    is OutgoingPayment.Details.SwapOut -> "Swap to a Bitcoin address"
                }
                is IncomingPayment -> when (val origin = payment.origin) {
                    is IncomingPayment.Origin.Invoice -> data.metadata.userDescription ?: origin.paymentRequest.description ?: origin.paymentRequest.descriptionHash?.toHex()
                    is IncomingPayment.Origin.KeySend -> "Spontaneous payment"
                    is IncomingPayment.Origin.SwapIn, is IncomingPayment.Origin.DualSwapIn -> "On-chain swap deposit"
                }
                else -> null
            }?.takeIf { it.isNotBlank() },
            fallbackValue = stringResource(id = R.string.paymentdetails_no_description)
        )

        if (payment is OutgoingPayment) {
            Spacer(modifier = Modifier.height(8.dp))
            DetailsRow(
                label = stringResource(id = R.string.paymentdetails_destination_label),
                value = when (val details = payment.details) {
                    is OutgoingPayment.Details.Normal -> details.paymentRequest.nodeId.toString()
                    is OutgoingPayment.Details.ChannelClosing -> details.closingAddress
                    is OutgoingPayment.Details.KeySend -> null
                    is OutgoingPayment.Details.SwapOut -> details.address
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        DetailsRow(
            label = stringResource(id = R.string.paymentdetails_fees_label),
            value = payment.fees.toPrettyString(LocalBitcoinUnit.current, withUnit = true, mSatDisplayPolicy = MSatDisplayPolicy.SHOW)
        )

        payment.errorMessage()?.let { errorMessage ->
            Spacer(modifier = Modifier.height(8.dp))
            DetailsRow(
                label = stringResource(id = R.string.paymentdetails_error_label),
                value = errorMessage
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                text = stringResource(id = R.string.paymentdetails_details_button),
                textStyle = MaterialTheme.typography.button.copy(fontSize = 14.sp),
                icon = R.drawable.ic_tool,
                modifier = Modifier.weight(1f),
                space = 8.dp,
                onClick = {
                    onDetailsClick(data.id())
                })
            Button(
                text = stringResource(id = R.string.paymentdetails_edit_button),
                textStyle = MaterialTheme.typography.button.copy(fontSize = 14.sp),
                icon = R.drawable.ic_edit,
                modifier = Modifier.weight(1f),
                space = 8.dp,
                onClick = { showEditDescriptionDialog = true })
        }
    }

    if (showEditDescriptionDialog) {
        EditPaymentDetails(
            initialDescription = data.metadata.userDescription,
            onConfirm = {
                onMetadataDescriptionUpdate(data.id(), it?.trim()?.takeIf { it.isNotBlank() })
                showEditDescriptionDialog = false
            },
            onDismiss = { showEditDescriptionDialog = false }
        )
    }
}

@Composable
private fun PaymentStatus(
    payment: WalletPayment,
    fromEvent: Boolean,
) {
    when (payment) {
        is OutgoingPayment -> when (payment.status) {
            is OutgoingPayment.Status.Pending -> PaymentStatusIcon(
                message = stringResource(id = R.string.paymentdetails_status_sent_pending),
                imageResId = R.drawable.ic_payment_details_pending_static,
                isAnimated = false,
                color = mutedTextColor()
            )
            is OutgoingPayment.Status.Completed.Failed -> PaymentStatusIcon(
                message = stringResource(id = R.string.paymentdetails_status_sent_failed),
                imageResId = R.drawable.ic_payment_details_failure_static,
                isAnimated = false,
                color = negativeColor()
            )
            is OutgoingPayment.Status.Completed.Succeeded -> PaymentStatusIcon(
                message = stringResource(id = R.string.paymentdetails_status_sent_successful),
                imageResId = if (fromEvent) R.drawable.ic_payment_details_success_animated else R.drawable.ic_payment_details_success_static,
                isAnimated = fromEvent,
                color = positiveColor(),
                timestamp = payment.completedAt()
            )
        }
        is IncomingPayment -> when (payment.received) {
            null -> PaymentStatusIcon(
                message = stringResource(id = R.string.paymentdetails_status_received_pending),
                imageResId = R.drawable.ic_payment_details_pending_static,
                isAnimated = false,
                color = mutedTextColor()
            )
            else -> PaymentStatusIcon(
                message = stringResource(id = R.string.paymentdetails_status_received_successful),
                imageResId = if (fromEvent) R.drawable.ic_payment_details_success_animated else R.drawable.ic_payment_details_success_static,
                isAnimated = fromEvent,
                color = positiveColor(),
                timestamp = payment.received?.receivedAt
            )
        }
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun PaymentStatusIcon(
    message: String,
    timestamp: Long? = null,
    isAnimated: Boolean,
    imageResId: Int,
    color: Color,
) {
    val scope = rememberCoroutineScope()
    var atEnd by remember { mutableStateOf(false) }
    Image(
        painter = if (isAnimated) {
            rememberAnimatedVectorPainter(AnimatedImageVector.animatedVectorResource(imageResId), atEnd)
        } else {
            painterResource(id = imageResId)
        },
        contentDescription = null,
        colorFilter = ColorFilter.tint(color),
        modifier = Modifier.size(90.dp)
    )
    if (isAnimated) {
        LaunchedEffect(key1 = Unit) {
            scope.launch {
                delay(150)
                atEnd = true
            }
        }
    }
    Spacer(Modifier.height(16.dp))
    Text(text = message.uppercase(), style = MaterialTheme.typography.body2)
    timestamp?.let {
        Text(text = timestamp.toAbsoluteDateString(), style = MaterialTheme.typography.caption)
    }
}

@Composable
private fun DetailsRow(
    label: String,
    value: String?,
    fallbackValue: String = stringResource(id = R.string.utils_unknown),
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Row(
        modifier = Modifier.padding(horizontal = 24.dp).widthIn(max = 400.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.caption.copy(textAlign = TextAlign.End), modifier = Modifier.weight(.7f))
        Spacer(Modifier.width(8.dp))
        Text(
            text = value ?: fallbackValue,
            style = MaterialTheme.typography.body1.copy(fontStyle = if (value == null) FontStyle.Italic else FontStyle.Normal),
            modifier = Modifier.weight(1f),
            maxLines = maxLines,
            overflow = overflow,
        )
    }
}

@Composable
private fun EditPaymentDetails(
    initialDescription: String?,
    onConfirm: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    var description by rememberSaveable { mutableStateOf(initialDescription) }
    Dialog(
        onDismiss = onDismiss,
        buttons = {
            Button(onClick = onDismiss, text = stringResource(id = R.string.btn_cancel))
            Button(
                onClick = {
                    onConfirm(description)
                },
                text = stringResource(id = R.string.btn_save)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(text = stringResource(id = R.string.paymentdetails_edit_dialog_title))
            Spacer(modifier = Modifier.height(16.dp))
            TextInput(
                modifier = Modifier.fillMaxWidth(),
                text = description ?: "",
                onTextChange = { description = it.takeIf { it.isNotBlank() } },
            )
        }
    }
}